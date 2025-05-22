package com.ozantok.combinia.domain.usecase

import com.ozantok.combinia.domain.model.OutfitPost
import com.ozantok.combinia.domain.repository.OutfitRepository

class GetOutfitPostsUseCase(private val repository: OutfitRepository) {
    suspend operator fun invoke(): Result<List<OutfitPost>> = repository.getAllOutfitPosts()
}