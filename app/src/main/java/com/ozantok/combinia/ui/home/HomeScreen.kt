package com.ozantok.combinia.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ozantok.combinia.components.OutfitPostCard
import com.ozantok.combinia.domain.model.OutfitPost
import com.ozantok.combinia.ui.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val posts = viewModel.posts.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadOutfitPosts()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            OutfitPostCard(
                postId = post.postId,
                username = post.username,
                userProfileUrl = post.userProfileUrl,
                outfitImageUrl = post.outfitImageUrl,
                description = post.description,
                isLikedByUser = post.likedBy.contains(viewModel.currentUserId),
                isFavoritedByUser = post.favoritedBy.contains(viewModel.currentUserId),
                homeViewModel = viewModel
            )
        }
    }
}

data class OutfitUiModel(
    val postId: String,
    val username: String,
    val userProfileUrl: String,
    val outfitImageUrl: String,
    val description: String,
    val isLikedByUser: Boolean = false,
    val isFavoritedByUser: Boolean = false
)