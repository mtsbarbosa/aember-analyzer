#!/usr/bin/env python3
"""Shallow end-to-end smoke test against a locally running aember-analyzer.

    python3 tools/smoke-test.py [--base-url URL] [--keep] [--skip-ladder] [--import UUID ...]

Exercises most REST endpoints with tiny workloads (a few seconds total), using
decks that are ALREADY imported (it never needs the Master Vault unless you pass
--import). Everything it creates (pool, batches) is torn down at the end via the
DELETE endpoints unless --keep is given. Exit code 0 = all passed, 1 = failures.
Needs only the Python 3 standard library.
"""
import argparse, json, sys, time, urllib.request, urllib.error, uuid

class Smoke:
    def __init__(self, base):
        self.base, self.results = base.rstrip("/"), []

    def call(self, method, path, body=None, headers=None):
        data = json.dumps(body).encode() if body is not None else None
        h = {"Content-Type": "application/json", **(headers or {})}
        req = urllib.request.Request(self.base + path, data=data, method=method, headers=h)
        try:
            with urllib.request.urlopen(req, timeout=300) as r:
                raw = r.read().decode()
                return r.status, (json.loads(raw) if raw else None)
        except urllib.error.HTTPError as e:
            raw = e.read().decode()
            try: return e.code, json.loads(raw)
            except ValueError: return e.code, raw

    def check(self, name, method, path, expect=200, body=None, headers=None, verify=None):
        t = time.time()
        try:
            status, payload = self.call(method, path, body, headers)
            ok = status == expect
            note = "" if ok else f"expected {expect}, got {status}: {str(payload)[:150]}"
            if ok and verify:
                try:
                    if verify(payload) is False: ok, note = False, "verification failed"
                except Exception as e:  # noqa
                    ok, note = False, f"verification error: {e!r}"
        except Exception as e:  # connection errors etc.
            ok, note, payload = False, repr(e), None
        self.results.append((name, ok, note))
        print(f"  {'PASS' if ok else 'FAIL'}  {name} ({time.time()-t:.1f}s){'  - ' + note if note else ''}")
        return payload if ok else None

def wait_batch(s, bid, timeout=120):
    end = time.time() + timeout
    while time.time() < end:
        _, b = s.call("GET", f"/api/mass-simulations/{bid}")
        if isinstance(b, dict) and b.get("status") in ("COMPLETED", "FAILED"): return b
        time.sleep(1)
    return None

def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--base-url", default="http://localhost:8080")
    ap.add_argument("--keep", action="store_true", help="do not tear down created data")
    ap.add_argument("--skip-ladder", action="store_true", help="skip POST /api/ladder/rerun (needs a 2+ deck pool)")
    ap.add_argument("--import", dest="imports", nargs="*", default=[], metavar="UUID",
                    help="also test POST /api/decks/import/{uuid} (needs network/Master Vault)")
    a = ap.parse_args()
    s = Smoke(a.base_url)
    created_batches, pool_key = [], "smoke-" + uuid.uuid4().hex[:8]

    print(f"Smoke test against {s.base}")
    print("[decks]")
    decks = s.check("list decks", "GET", "/api/decks", verify=lambda d: isinstance(d, list))
    if not decks or len(decks) < 3:
        print("\nNeed at least 3 imported decks (import some first, e.g. --import <uuid>).")
        for u in a.imports: s.check(f"import {u}", "POST", f"/api/decks/import/{u}")
        sys.exit(1)
    for u in a.imports: s.check(f"import {u}", "POST", f"/api/decks/import/{u}")
    d = [x["uuid"] for x in decks[:3]]
    s.check("filter decks by house", "GET", "/api/decks?house=" + decks[0]["houses"][0])
    s.check("filter decks by expansion", "GET", "/api/decks?expansion=341")
    s.check("get deck", "GET", f"/api/decks/{d[0]}", verify=lambda x: x["uuid"] == d[0])

    print("[single simulation]")
    s.check("simulate (summary)", "GET", f"/api/simulate/{d[0]}/{d[1]}?seed=42", verify=lambda x: "statistics" in x or "winner" in str(x).lower())
    s.check("simulate (X-Debug)", "GET", f"/api/simulate/{d[0]}/{d[1]}?seed=42&aiA=greedy&aiB=random", headers={"X-Debug": "true"})

    print("[two-deck batch]")
    b = s.check("start batch (202)", "POST", "/api/mass-simulations", expect=202,
                body={"deckUuidA": d[0], "deckUuidB": d[1], "matchCount": 3, "aiA": "tactical", "aiB": "tactical", "recordLogs": True, "baseSeed": 1})
    if b:
        bid = b["uuid"]; created_batches.append(bid)
        done = wait_batch(s, bid)
        s.results.append(("batch completes", bool(done and done["status"] == "COMPLETED"), "" if done else "timeout"))
        print(f"  {'PASS' if done and done['status']=='COMPLETED' else 'FAIL'}  batch completes")
        s.check("list batches", "GET", "/api/mass-simulations", verify=lambda l: any(x["uuid"] == bid for x in l))
        s.check("batch details", "GET", f"/api/mass-simulations/{bid}")
        ms = s.check("batch matches", "GET", f"/api/mass-simulations/{bid}/matches", verify=lambda m: len(m) == 3)
        if ms: s.check("match log", "GET", f"/api/mass-simulations/{bid}/matches/{ms[0]['id']}/logs")
    s.check("unknown batch -> 400", "GET", f"/api/mass-simulations/{uuid.uuid4()}", expect=400)

    print("[multi-deck batch + ratings]")
    m = s.check("start multi batch (202)", "POST", "/api/mass-simulations/multi", expect=202,
                body={"deckUuids": d, "matchesPerPairing": 2, "ai": "tactical", "seatAlternation": True, "baseSeed": 7})
    if m:
        mid = m["uuid"]; created_batches.append(mid)
        done = wait_batch(s, mid)
        print(f"  {'PASS' if done and done['status']=='COMPLETED' else 'FAIL'}  multi batch completes")
        s.results.append(("multi batch completes", bool(done and done["status"] == "COMPLETED"), ""))
        s.check("ratings", "GET", f"/api/mass-simulations/{mid}/ratings", verify=lambda r: len(r) == 3)
        s.check("tier validation", "POST", f"/api/mass-simulations/{mid}/tier-validation",
                body={"tiers": {d[0]: 3.0, d[1]: 2.0, d[2]: 1.0}})
    s.check("multi with 1 deck -> 400", "POST", "/api/mass-simulations/multi", expect=400,
            body={"deckUuids": [d[0]], "matchesPerPairing": 1})

    print("[pools]  (temporary pool: " + pool_key + ")")
    for x in d: s.check("add deck to pool", "POST", f"/api/pools/{pool_key}/decks", body={"deckUuid": x})
    s.check("get pool", "GET", f"/api/pools/{pool_key}", verify=lambda p: p["deckCount"] == 3)
    s.check("rate deck vs pool", "POST", f"/api/pools/{pool_key}/ratings/{d[0]}?matchesPerPairing=2&baseSeed=0")
    s.check("rating history", "GET", f"/api/pools/{pool_key}/ratings/{d[0]}", verify=lambda h: len(h) >= 1)
    s.check("remove deck from pool", "DELETE", f"/api/pools/{pool_key}/decks/{d[2]}", verify=lambda p: p["deckCount"] == 2)

    if not a.skip_ladder:
        print("[ladder]")
        r = s.check("ladder rerun (tiny pool)", "POST", "/api/ladder/rerun",
                    body={"pool": pool_key, "matchesPerPairing": 2, "baseSeed": 3, "timeoutSeconds": 120},
                    verify=lambda x: x["status"] == "COMPLETED" and not x["timedOut"] and len(x["ratings"]) == 2)
        if r: created_batches.append(r["batchUuid"])
        s.check("ladder: bad pool key -> 400", "POST", "/api/ladder/rerun", expect=400, body={"pool": "../x"})

    if not a.keep:
        print("[teardown]")
        for bid in created_batches: s.check(f"delete batch {bid[:8]}", "DELETE", f"/api/mass-simulations/{bid}", expect=204)
        s.check("delete pool", "DELETE", f"/api/pools/{pool_key}", expect=204)
        s.check("delete pool again -> 400", "DELETE", f"/api/pools/{pool_key}", expect=400)
        if created_batches:
            s.check("batch is gone", "GET", f"/api/mass-simulations/{created_batches[0]}", expect=400)
    else:
        print(f"[kept] pool={pool_key} batches={created_batches}")

    failed = [r for r in s.results if not r[1]]
    print(f"\n{len(s.results)-len(failed)}/{len(s.results)} checks passed")
    sys.exit(1 if failed else 0)

if __name__ == "__main__":
    main()
