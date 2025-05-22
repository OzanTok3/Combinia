package com.ozantok.combinia.domain.model


data class OutfitPost(
    val postId: String,
    val userId: String,
    val username: String,
    val userProfileUrl: String,
    val outfitImageUrl: String,
    val description: String,
    val likedBy: List<String> = emptyList(),
    val favoritedBy: List<String> = emptyList()
)