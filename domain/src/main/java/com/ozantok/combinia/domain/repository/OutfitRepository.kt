package com.ozantok.combinia.domain.repository


import com.ozantok.combinia.domain.model.Comment
import com.ozantok.combinia.domain.model.OutfitPost

interface OutfitRepository {

    suspend fun toggleLike(postId: String): Result<Unit>

    suspend fun toggleFavorite(postId: String): Result<Unit>

    suspend fun getComments(postId: String, limit: Int = 10): Result<List<Comment>>

    suspend fun addComment(
        postId: String,
        text: String,
        userId: String,
        username: String
    ): Result<Unit>

    suspend fun getLikeCount(postId: String): Result<Int>
    suspend fun getCommentCount(postId: String): Result<Int>

    suspend fun getAllOutfitPosts(): Result<List<OutfitPost>>
}