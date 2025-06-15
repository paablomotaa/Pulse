package com.pmgdev.pulse.repository.firebaserepository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.repository.model.Comment
import com.pmgdev.pulse.repository.model.Fine
import com.pmgdev.pulse.repository.model.Post
import com.pmgdev.pulse.repository.model.TypeFine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 *
 * Post repository
 *
 * Sentencias para acceder a la base de datos y a la coleccion de Posts y la subcoleccion Comments.
 *
 */

class PostRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firestorage: FirebaseStorage
) {
    /**
     *
     * addPost
     *
     * Añade un post a la base de datos. Sube la información a Firestore y la foto a Storage
     *
     * @param uri
     * @param uiduser
     * @param username
     * @param description
     *
     */
    suspend fun addPost(uri: Uri, uiduser: String, username: String, description: String) {
        val uid = UUID.randomUUID()
        val filename = "post/${uid}.jpg"
        val ref = firestorage.reference.child(filename)

        ref.putFile(uri).await()
        val url = ref.downloadUrl.await()

        val post = Post(
            uid = uid.toString(),
            uiduser = uiduser,
            username = username,
            imagePost = url.toString(),
            description = description
        )
        val refpost = firestore.collection("posts").add(post).await()

        refpost.update("uid", refpost.id)
    }

    /**
     *
     * getPost
     *
     * Recoge un post de la base de datos.
     *
     * @param uid
     *
     */
    suspend fun getPost(uid: String): Post? {
        val snapshot = firestore.collection("posts").document(uid).get().await()

        if (snapshot.exists()) {
            val post = snapshot.toObject(Post::class.java)
            if (post != null) {
                return post
            } else {
                Log.e("ERROR", "es null")
            }
        } else {
            return null
        }
        return null
    }

    /**
     *
     * getPosts
     *
     * Recoge todos los posts de la base de datos.
     *
     *
     */
    suspend fun getPosts(): List<Post> {
        return try {
            val snapshot = firestore.collection("posts")
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener posts: $e")
            emptyList()
        }
    }

    /**
     *
     * getPostsFromUser
     *
     * Recoge todos los posts de un usuario de la base de datos.
     *
     */
    suspend fun getPostsFromUser(userId:String):List<Post>{
        return try {
            val snapshot = firestore.collection("posts")
                .whereEqualTo("uiduser",userId)
                .orderBy("date").get().await()

            return snapshot.toObjects(Post::class.java)
        } catch (e: Exception){
            Log.d("Firestore","Error al obtener posts: $e")
            emptyList()
        }
    }

    /**
     *
     * observePosts
     *
     * Observa los cambios en los posts de la base de datos. Los pasa por un callback.
     *
     * @param onPostsChanged
     *
     */

    fun observePosts(onPostsChanged: (List<Post>) -> Unit) {
        firestore.collection("posts")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val posts = snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                onPostsChanged(posts)
                Log.d("Firestore", "SnapshotListener activado para posts")
            }
    }

    /**
     *
     * observeComments
     *
     * Observa los cambios en los comentarios de un post. Los pasa por un callback.
     *
     * @param postId
     * @param onCommentsChanged
     *
     *
     */
    fun observeComments(postId: String, onCommentsChanged: (List<Comment>) -> Unit) {
        firestore
            .collection("posts")
            .document(postId)
            .collection("comments")
            .orderBy("date")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val comments = snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
                onCommentsChanged(comments)
                Log.d("Firestore", "SnapshotListener activado para $postId")
            }

    }

    /**
     *
     * getComments
     *
     * Recoge los comentarios de un post.
     *
     * @param postId
     *
     */
    suspend fun getComments(postId: String): List<Comment> {
        val snapshot = firestore
            .collection("posts")
            .document(postId)
            .collection("comments")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
    }

    /**
     *
     * addComments
     *
     * Añade un comentario a un post.
     *
     * @param postId
     * @param uidUser
     * @param username
     * @param content
     *
     */
    suspend fun addComments(postId: String, uidUser: String, username: String, content: String) {
        val comment = Comment(
            uidUser = uidUser,
            username = username,
            content = content
        )

        firestore.collection("posts").document(postId).collection("comments")
            .add(comment) //Añado una subcolección
            .addOnSuccessListener {
                firestore.collection("posts")
                    .document(postId)
                    .update(
                        "comments",
                        FieldValue.increment(1)
                    ) //Para incrementar los comentarios que tiene el post
            }
    }

    /**
     *
     * deletePost
     *
     * Borra un post de la base de datos.
     *
     * @param postId
     *
     */
    suspend fun deletePost(postId:String){
        firestore.collection("posts").document(postId).delete()
    }
    suspend fun likePost(postId: String, userId: String) {

        val likeRef = firestore.collection("posts")
            .document(postId)
            .collection("likes")
            .document(userId)

        firestore.collection("posts").document(postId).update("likes", FieldValue.increment(1))

        likeRef.set(emptyMap<String, Any>()) //Para que no añada ningun campo ya que el userUID lo estamos guardando en el nombre.
    }

    /**
     *
     * unlikePost
     *
     * Borra un like de un post.
     *
     * @param postId
     * @param userId
     *
     */
    suspend fun unlikePost(postId: String, userId: String) {
        val likeRef = firestore.collection("posts")
            .document(postId)
            .collection("likes")
            .document(userId)

        firestore.collection("posts").document(postId).update("likes", FieldValue.increment(-1))

        likeRef.delete().await()
    }

    /**
     *
     * isPostLikedByUser
     *
     * Comprueba si un usuario ha dado like a un post.
     *
     * @param postId
     * @param userId
     *
     */

    suspend fun isPostLikedByUser(postId: String, userId: String) : Boolean {
        val snapshot = firestore.collection("posts")
            .document(postId)
            .collection("likes")
            .document(userId)
            .get()
            .await()

        return snapshot.exists()
    }

    /**
     *
     * createFine
     *
     * Crea una denuncia de un post.
     *
     * @param fine
     */
    suspend fun createFine(fine: Fine) : Result<Unit> {

        try{
            firestore.collection("fines").add(fine).await()
            return Result.success(Unit)
        }
        catch (e:Exception){
            return Result.failure(e)
        }
    }
}