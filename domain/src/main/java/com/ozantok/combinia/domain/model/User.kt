package com.ozantok.combinia.domain.model

data class User(
    val id: String,
    val username: String,
    val profilePhotoUrl: String,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList()
)