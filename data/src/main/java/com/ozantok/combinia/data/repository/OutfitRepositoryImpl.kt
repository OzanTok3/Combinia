package com.ozantok.combinia.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ozantok.combinia.domain.model.Comment
import com.ozantok.combinia.domain.model.OutfitPost
import com.ozantok.combinia.domain.repository.OutfitRepository
import kotlinx.coroutines.tasks.await
import java.util.Date

class OutfitRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : OutfitRepository {

    override suspend fun toggleLike(postId: String): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
            val postRef = firestore.collection("outfits").document(postId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val likedBy = snapshot.get("likedBy") as? List<*> ?: emptyList<Any>()
                val updatedLikes = if (likedBy.contains(userId)) {
                    likedBy.filterNot { it == userId }
                } else {
                    likedBy + userId
                }
                transaction.update(postRef, "likedBy", updatedLikes)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(postId: String): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
            val postRef = firestore.collection("outfits").document(postId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val favoritedBy = snapshot.get("favoritedBy") as? List<*> ?: emptyList<Any>()
                val updatedFavorites = if (favoritedBy.contains(userId)) {
                    favoritedBy.filterNot { it == userId }
                } else {
                    favoritedBy + userId
                }
                transaction.update(postRef, "favoritedBy", updatedFavorites)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getComments(postId: String, limit: Int): Result<List<Comment>> {
        return try {
            val commentsSnapshot = firestore.collection("outfits")
                .document(postId)
                .collection("comments")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val comments = commentsSnapshot.documents.mapNotNull { doc ->
                val id = doc.id
                val userId = doc.getString("userId") ?: return@mapNotNull null
                val username = doc.getString("username") ?: return@mapNotNull null
                val text = doc.getString("text") ?: return@mapNotNull null
                val timestamp = doc.getTimestamp("timestamp")?.toDate() ?: Date()

                Comment(
                    commentId = id,
                    userId = userId,
                    username = username,
                    text = text,
                    timestamp = timestamp
                )
            }

            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addComment(
        postId: String,
        text: String,
        userId: String,
        username: String
    ): Result<Unit> {
        return try {
            val commentId = firestore.collection("outfits")
                .document(postId)
                .collection("comments")
                .document().id

            val comment = hashMapOf(
                "commentId" to commentId,
                "userId" to userId,
                "username" to username,
                "text" to text,
                "timestamp" to Timestamp.now()
            )

            firestore.collection("outfits")
                .document(postId)
                .collection("comments")
                .document(commentId)
                .set(comment)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getLikeCount(postId: String): Result<Int> {
        return try {
            val snapshot = firestore.collection("outfits").document(postId).get().await()
            val likedBy = snapshot.get("likedBy") as? List<*> ?: emptyList<Any>()
            Result.success(likedBy.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCommentCount(postId: String): Result<Int> {
        return try {
            val snapshot = firestore.collection("outfits")
                .document(postId)
                .collection("comments")
                .get()
                .await()
            Result.success(snapshot.size())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllOutfitPosts(): Result<List<OutfitPost>> {
        return try {
            val snapshot = firestore.collection("outfits").get().await()
            val posts = snapshot.documents.mapNotNull { doc ->
                val postId = doc.id
                val userId = doc.getString("userId") ?: return@mapNotNull null
                val username = doc.getString("username") ?: "Unknown"
                val userProfileUrl = doc.getString("userProfileUrl") ?: ""
                val outfitImageUrl = doc.getString("outfitImageUrl") ?: ""
                val description = doc.getString("description") ?: ""
                val likedBy = doc.get("likedBy") as? List<String> ?: emptyList()
                val favoritedBy = doc.get("favoritedBy") as? List<String> ?: emptyList()

                OutfitPost(
                    postId, userId, username, userProfileUrl,
                    outfitImageUrl, description, likedBy, favoritedBy
                )
            }
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}