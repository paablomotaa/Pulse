package com.pmgdev.pulse.repository.firebaserepository

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.repository.model.Follow
import com.pmgdev.pulse.repository.model.Notification
import com.pmgdev.pulse.repository.model.User
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 *
 * User repository
 *
 * Sentencias para acceder a la base de datos y a la coleccion de Users.
 *
 */

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) {
    suspend fun addUser(user: User): Result<Unit> {
        return try {
            val userRef = firestore.collection("users").document(user.uid)
            userRef.set(user).await() //es para hacer que la operación sea asíncrona
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUser(uid: String): User? {
        val snapshot = firestore.collection("users").document(uid).get().await()

        if (snapshot.exists()) {
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                return user
            } else {
                Log.e("FirestoreError", "Usuario no encontrado en Firestore")
                return null
            }
        } else {
            Log.e("FirestoreError", "El documento con el UID $uid no existe en Firestore")
            return null
        }
    }
    fun editUser(userId: String, user: User) {
        if (user.profileImage.startsWith("content://") || user.profileImage.startsWith("file://")) {
            val imageUri = user.profileImage.toUri()
            val ref = firebaseStorage.reference.child("imageperf/${UUID.randomUUID()}.jpg")
            ref.putFile(imageUri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val updatedUser = user.copy(profileImage = downloadUrl.toString())
                        firestore.collection("users").document(userId).set(updatedUser)
                    }
                }
                .addOnFailureListener {
                    Log.e("EDIT_USER", "Error al subir la imagen", it)
                }
        } else {
            firestore.collection("users").document(userId).set(user)
        }
    }


    suspend fun checkemailexists(email:String): Boolean{
        val email = firestore.collection("users").whereEqualTo("email",email).get().await()

        if(email.isEmpty){
            return false
        }
        else{
            return true
        }
    }
    suspend fun checkusernameexists(username:String):Boolean{
        val username = firestore.collection("users").whereEqualTo("username",username).get().await()

        if(username.isEmpty){
            return false
        }
        else{
            return true
        }
    }



    suspend fun followUser(
        currentUid: String,
        targetUid: String,
        targetUsername: String,
        currentUsername: String
    ) {
        val followData = Follow(uid = targetUid, username = targetUsername)
        val followerData = Follow(uid = currentUid, username = currentUsername)

        val followingRef = firestore.collection("users").document(currentUid)
            .collection("following").document(targetUid)
        val followingIncrement = firestore.collection("users").document(currentUid)
        followingIncrement.update("following", FieldValue.increment(1))

        val followerRef = firestore.collection("users").document(targetUid)
            .collection("followers").document(currentUid)
        val followerIncrement = firestore.collection("users").document(targetUid)
        followerIncrement.update("followers", FieldValue.increment(1))

        followingRef.set(followData)
        followerRef.set(followerData)
    }
    suspend fun unfollowUser(currentUid: String, targetUid: String) {
        val followingRef = firestore.collection("users").document(currentUid)
            .collection("following").document(targetUid)
        val followingDecrement = firestore.collection("users").document(currentUid)
        followingDecrement.update("following", FieldValue.increment(-1))
        Log.d("BORRADO",targetUid)

        val followerRef = firestore.collection("users").document(targetUid)
            .collection("followers").document(currentUid)
        val followersDecrement = firestore.collection("users").document(targetUid)
        followersDecrement.update("followers", FieldValue.increment(-1))
        Log.d("BORRADO",currentUid)

        followingRef.delete().await()
        followerRef.delete().await()
    }

    suspend fun changeEmail(newEmail: String): Result<Boolean> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))

            user.updateEmail(newEmail).await()

            firestore.collection("users")
                .document(user.uid)
                .update("email", newEmail)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun deleteAccount(): Result<Boolean>{
        return try{
            val user = auth.currentUser ?: return Result.failure(Exception("Usuario no autenticado"))

            //Borro publicaciones
            val posts = firestore.collection("posts")
                .whereEqualTo("userId", user.uid)
                .get()
                .await()

            for (document in posts.documents) {
                firestore.collection("posts").document(document.id).delete().await()
            }

            //Borro al usuario de firestore

            firestore.collection("users")
                .document(user.uid)
                .delete()
                .await()

            //Borro al usuario de auth
            user.delete()
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun getNotificationsFromUser(userId:String) : List<Notification>{
        val notificationsRef = firestore.collection("users").document(userId).collection("notifications").get().await()
        return notificationsRef.toObjects(Notification::class.java)
    }
    suspend fun pushNotificationFromUser(userId:String, notification: Notification):Result<Unit>{

        return try {
            val notificationRef = firestore.collection("users").document(userId).collection("notifications").add(notification)
            notificationRef.await()

            return Result.success(Unit)
        } catch (e: Exception){
            return Result.failure(e)
        }
    }
    suspend fun saveFcmTokenForCurrentUser(userId: String) {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            val userDocRef = firestore.collection("users").document(userId)
            userDocRef.update("fcmToken", token)
                .addOnSuccessListener {
                }
        }
    }
}