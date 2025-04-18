package com.pmgdev.pulse.repository.firebaserepository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pmgdev.pulse.repository.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 *
 * User repository
 *
 * Sentencias para acceder a la base de datos.
 *
 */

class UserRepository @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth
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
        @RequiresApi(Build.VERSION_CODES.O)
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
    }