package com.ozantok.combinia.components

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ozantok.combinia.R
import com.ozantok.combinia.domain.model.Comment
import com.ozantok.combinia.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitPostCard(
    postId: String,
    username: String,
    userProfileUrl: String,
    outfitImageUrl: String,
    description: String,
    isLikedByUser: Boolean,
    isFavoritedByUser: Boolean,
    homeViewModel: HomeViewModel
) {
    val commentsMap by homeViewModel.commentsMap.collectAsState()
    val visibilityMap by homeViewModel.commentVisibilityMap.collectAsState()
    val comments = commentsMap[postId].orEmpty()
    val areCommentsVisible = visibilityMap[postId] ?: false
    var commentText by remember { mutableStateOf(TextFieldValue("")) }
    var isLiked by remember { mutableStateOf(isLikedByUser) }
    var isFavorited by remember { mutableStateOf(isFavoritedByUser) }

    val likeCounts by homeViewModel.likeCounts.collectAsState()
    val commentCounts by homeViewModel.commentCounts.collectAsState()

    val likeCount = likeCounts[postId] ?: 0
    val commentCount = commentCounts[postId] ?: 0
    var localLikeCount by remember { mutableStateOf(likeCount) }


    LaunchedEffect(Unit) {
        homeViewModel.loadLikeCount(postId)
        homeViewModel.loadCommentCount(postId)
    }

    Card(
        shape = RoundedCornerShape(12.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9E5)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(userProfileUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )
                Text(username, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter(outfitImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(description)

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LikeButton(isLiked = isLiked, onClick = {
                        isLiked = !isLiked
                        localLikeCount += if (isLiked) 1 else -1
                        homeViewModel.toggleLike(postId)
                    })
                    Text(
                        text = likeCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    IconButton(onClick = {
                        homeViewModel.toggleCommentVisibility(postId)
                        if (!areCommentsVisible) homeViewModel.loadComments(postId)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_comment),
                            contentDescription = "Comment",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        text = commentCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                FavoriteButton(isFavorited = isFavorited, onClick = {
                    isFavorited = !isFavorited
                    homeViewModel.toggleFavorite(postId)
                })
            }
            if (areCommentsVisible) {
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    comments.forEach { comment ->
                        CommentItem(comment)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(
                        Color(0xFFFFF9E5)
                    )
                ) {
                    TextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Yorum girin...") },
                        modifier = Modifier
                            .weight(1f)
                            .background(
                               Color.Transparent
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFFFF9E5), // arka plan
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (commentText.text.isNotBlank()) {
                                homeViewModel.addComment(postId, commentText.text)
                                commentText = TextFieldValue("")
                            }
                        }) {
                        Text("Send")
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "${comment.username}: ",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = comment.text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun IconTextButton(
    icon: ImageVector, text: String, onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Icon(imageVector = icon, contentDescription = text)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text)
    }
}

@Composable
fun LikeButton(isLiked: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(
                id = if (isLiked) R.drawable.icon_heart_fill else R.drawable.icon_heart
            ), contentDescription = "Like",
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun FavoriteButton(isFavorited: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(
                id = if (isFavorited) R.drawable.icon_favorite_fill else R.drawable.icon_favorite
            ), contentDescription = "Favorite",
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified

        )
    }
}
