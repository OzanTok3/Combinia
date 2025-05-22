package com.ozantok.combinia.domain.usecase

import com.ozantok.combinia.domain.repository.OutfitRepository

class AddCommentUseCase(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(
        postId: String,
        text: String,
        userId: String,
        username: String
    ) = repository.addComment(postId, text, userId, username)
}