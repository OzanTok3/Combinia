package com.ozantok.combinia.domain.usecase


import com.ozantok.combinia.domain.repository.OutfitRepository

class GetCommentsUseCase(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(postId: String, limit: Int = 10) = repository.getComments(postId, limit)
}