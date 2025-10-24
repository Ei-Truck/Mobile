package com.example.eitruck.domain

interface FilterStrategy {
    val showRegiao: Boolean
    val showSegmento: Boolean
    val showUnidade: Boolean
}