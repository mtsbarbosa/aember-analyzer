package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.model.CatalogCard

interface CardCatalogPort {
    fun loadAll(): List<CatalogCard>
    fun loadByExpansion(expansion: Int): List<CatalogCard>
    fun findById(id: String): CatalogCard?
}
