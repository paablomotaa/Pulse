package com.pmgdev.pulse.ui.feed

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


/**
 *
 * FeedViewModel
 *
 * Esto no es más que una vista previa muy lejana de lo que será realmente el feed.
 *
 */
@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    val fbStorage: FirebaseStorage,
    val firestore: FirebaseFirestore,
    val userRepository: UserRepository
) : ViewModel() {
    var state by mutableStateOf<FeedScreenState>(FeedScreenState.Loading)

    fun getNotices() {
        viewModelScope.launch {
            firestore.collection("posts")
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val posts = result.documents.mapNotNull { doc ->
                        doc.toObject(Post::class.java)
                    }

                    if (posts.isEmpty()) {
                        state = FeedScreenState.NoData
                        Log.e("ERROR","NO CONSIGUE LOS DATOS post is empty")
                    } else {
                        state = FeedScreenState.Success(posts)
                        Log.e("SUCCESS","CONSIGUE LOS DATOS")
                    }
                }
                .addOnFailureListener {
                    Log.e("ERROR","NO CONSIGUE LOS DATOS")
                    state = FeedScreenState.NoData // O puedes agregar un estado de error
                }
        }
    }

    init {
        getNotices()
    }
}