package com.aember.analyzer.domain.model

object CardSlug {

    fun fromTitle(title: String): String {
        return title.lowercase()
            .replace("'", "")
            .replace("[^a-z0-9]+".toRegex(), "-")
            .trim('-')
    }
}
