package com.ozantok.combinia.domain.usecase

import com.ozantok.combinia.domain.repository.OutfitRepository

class LikePostUseCase(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(postId: String) = repository.toggleLike(postId)
}