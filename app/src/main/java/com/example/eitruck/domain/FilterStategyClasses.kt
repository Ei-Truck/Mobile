package com.example.eitruck.domain

class AnalyticsManager : FilterStrategy {
    override val showRegiao = true
    override val showSegmento = true
    override val showUnidade = true
}

class SegmentsManager : FilterStrategy {
    override val showRegiao = true
    override val showSegmento = false
    override val showUnidade = true
}

class RegionsManager : FilterStrategy {
    override val showRegiao = false
    override val showSegmento = false
    override val showUnidade = true
}

class LocalManager : FilterStrategy {
    override val showRegiao = false
    override val showSegmento = false
    override val showUnidade = false
}
