package com.ozantok.combinia.domain.usecase

import com.ozantok.combinia.domain.repository.OutfitRepository

class GetCommentCountUseCase(private val repository: OutfitRepository) {
    suspend operator fun invoke(postId: String) = repository.getCommentCount(postId)
}
