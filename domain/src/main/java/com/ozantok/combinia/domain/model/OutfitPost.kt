package com.ozantok.combinia.domain.model

data class OutfitPost(
    val id: String,
    val userId: String,
    val imageUrls: List<String>,
    val description: String,
    val tags: List<String>,
    val timestamp: Long
)