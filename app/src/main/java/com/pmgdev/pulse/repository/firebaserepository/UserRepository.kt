package com.pmgdev.pulse.repository.firebaserepository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.repository.model.Follow
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
    suspend fun editUser(uid: String, user: User) {
        try {
            Log.d("EDIT_USER", "Inicio de edición para uid: $uid")
            Log.d("EDIT_USER", "URI de imagen recibida: ${user.profileImage}")

            val uidfirestorage = UUID.randomUUID()
            val filename = "imageperf/$uidfirestorage.jpg"
            val ref = firebaseStorage.reference.child(filename)

            // Subir imagen
            Log.d("EDIT_USER", "Subiendo imagen a: $filename")
            val uploadTask = ref.putFile(user.profileImage.toUri()).await()
            Log.d("EDIT_USER", "Imagen subida con éxito")

            // Obtener URL
            val url = ref.downloadUrl.await()
            Log.d("EDIT_USER", "URL de descarga obtenida: $url")

            // Crear copia del usuario con la nueva imagen
            val updatedUser = user.copy(profileImage = url.toString())
            Log.d("EDIT_USER", "Usuario actualizado: $updatedUser")

            // Guardar en Firestore
            firestore.collection("users").document(uid)
                .set(updatedUser, SetOptions.merge())
                .await()
            Log.d("EDIT_USER", "Usuario guardado correctamente en Firestore")

        } catch (e: Exception) {
            Log.e("EDIT_USER", "Error al editar usuario: ${e.message}", e)
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
}