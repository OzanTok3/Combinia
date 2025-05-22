package com.ozantok.combinia.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.domain.model.Comment
import com.ozantok.combinia.domain.model.OutfitPost
import com.ozantok.combinia.domain.usecase.AddCommentUseCase
import com.ozantok.combinia.domain.usecase.FavoritePostUseCase
import com.ozantok.combinia.domain.usecase.GetCommentCountUseCase
import com.ozantok.combinia.domain.usecase.GetCommentsUseCase
import com.ozantok.combinia.domain.usecase.GetLikeCountUseCase
import com.ozantok.combinia.domain.usecase.GetOutfitPostsUseCase
import com.ozantok.combinia.domain.usecase.LikePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val likePostUseCase: LikePostUseCase,
    private val favoritePostUseCase: FavoritePostUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getLikeCountUseCase: GetLikeCountUseCase,
    private val getCommentCountUseCase: GetCommentCountUseCase,
    private val getOutfitPostsUseCase: GetOutfitPostsUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    private val _commentsMap = MutableStateFlow<Map<String, List<Comment>>>(emptyMap())
    val commentsMap: StateFlow<Map<String, List<Comment>>> = _commentsMap

    private val _commentVisibilityMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val commentVisibilityMap: StateFlow<Map<String, Boolean>> = _commentVisibilityMap

    private val _likeCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val likeCounts: StateFlow<Map<String, Int>> = _likeCounts

    private val _commentCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val commentCounts: StateFlow<Map<String, Int>> = _commentCounts

    private val _posts = MutableStateFlow<List<OutfitPost>>(emptyList())
    val posts: StateFlow<List<OutfitPost>> = _posts

    fun loadOutfitPosts() {
        viewModelScope.launch {
            getOutfitPostsUseCase().onSuccess {
                _posts.value = it
            }
        }
    }

    fun loadLikeCount(postId: String) {
        viewModelScope.launch {
            getLikeCountUseCase(postId).onSuccess { count ->
                _likeCounts.update { oldMap ->
                    oldMap.toMutableMap().apply { put(postId, count) }
                }
            }
        }
    }

    fun loadCommentCount(postId: String) {
        viewModelScope.launch {
            getCommentCountUseCase(postId).onSuccess { count ->
                _commentCounts.update { oldMap ->
                    oldMap.toMutableMap().apply { put(postId, count) }
                }
            }
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            likePostUseCase(postId)
            loadLikeCount(postId)
        }
    }

    fun toggleFavorite(postId: String) {
        viewModelScope.launch {
            favoritePostUseCase(postId)
        }
    }

    fun loadComments(postId: String) {
        viewModelScope.launch {
            val result = getCommentsUseCase(postId)
            result.onSuccess { comments ->
                _commentsMap.value = _commentsMap.value.toMutableMap().apply {
                    put(postId, comments)
                }
            }
        }
    }

    fun addComment(postId: String, text: String) {
        val user = firebaseAuth.currentUser ?: return
        val userId = user.uid
        val username = user.displayName?.takeIf { it.isNotBlank() }
            ?: user.email?.substringBefore("@")
            ?: "Anonim"

        viewModelScope.launch {
            val result = addCommentUseCase(postId, text, userId, username)
            result.onSuccess {
                loadComments(postId)
                loadCommentCount(postId)
            }
        }
    }

    fun toggleCommentVisibility(postId: String) {
        val current = _commentVisibilityMap.value[postId] ?: false
        _commentVisibilityMap.value = _commentVisibilityMap.value.toMutableMap().apply {
            put(postId, !current)
        }
    }
}