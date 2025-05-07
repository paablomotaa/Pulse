package com.pmgdev.pulse.repository.firebaserepository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.repository.model.Comment
import com.pmgdev.pulse.repository.model.Post
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firestorage: FirebaseStorage
) {
    suspend fun addPost(uri: Uri,uiduser:String,username:String,description: String){
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

        refpost.update("uid",refpost.id)

    }
    suspend fun getPost(uid:String):Post?{
        val snapshot = firestore.collection("posts").document(uid).get().await()

        if(snapshot.exists()){
            val post = snapshot.toObject(Post::class.java)
            if(post != null){
                return post
            }
            else{
                Log.e("ERROR","es null")
            }
        }
        else{
            return null
        }
        return null
    }

    //Escuchable para actualizar la lista de comentarios
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
            }
    }

    suspend fun getComments(postId: String): List<Comment> {
        val snapshot = firestore
            .collection("posts")
            .document(postId)
            .collection("comments")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
    }
    suspend fun addComments(postId:String,uidUser:String,username:String,content:String){
        val comment = Comment(
            uidUser = uidUser,
            username = username,
            content = content
        )

        firestore.collection("posts").document(postId).collection("comments").add(comment) //Añado una subcolección
            .addOnSuccessListener {
                firestore.collection("posts")
                    .document(postId)
                    .update("comments", FieldValue.increment(1)) //Para incrementar los comentarios que tiene el post
            }
    }
}