package com.ozantok.combinia.domain.model

import java.util.Date

data class Comment(
    val commentId: String = "",
    val userId: String = "",
    val username: String = "",
    val text: String = "",
    val timestamp: Date = Date()
)