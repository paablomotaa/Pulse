package com.pmgdev.pulse.repository.firebaserepository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.pmgdev.pulse.repository.model.Chat
import com.pmgdev.pulse.repository.model.ChatPreview
import com.pmgdev.pulse.repository.model.Fine
import com.pmgdev.pulse.repository.model.Message
import com.pmgdev.pulse.repository.model.User
import com.pmgdev.pulse.utils.CryptoUtils
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 *
 * ChatRepository
 *
 * Sentencias para el acceso a la base de datos del chat.
 *
 */
class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun createChatIfNotExists(
        userId1: String,
        userId2: String,
        firstMessage: Message,
        onComplete: (String) -> Unit, //Para pasar mejor el resultado una vez se complete.
    ) {
        val db = FirebaseFirestore.getInstance()
        val chatsRef = db.collection("chats")

        chatsRef
            .whereEqualTo("participants", listOf(userId1, userId2))
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    // Chat ya existe
                    val chatId = result.documents.first().id
                    onComplete(chatId)
                } else {
                    // Chat no existe, crearlo
                    val newChatRef = chatsRef.document()
                    val chat = Chat(
                        id = newChatRef.id,
                        participants = listOf(userId1, userId2),
                        lastMessage = "",
                        lastMessageTimestamp = firstMessage.timestamp
                    )
                    val newMessageRef = newChatRef.collection("messages").document()

                    db.runBatch { batch ->
                        batch.set(newChatRef, chat)
                        batch.set(newMessageRef, firstMessage)
                    }.addOnSuccessListener {
                        onComplete(newChatRef.id)
                    }.addOnFailureListener{
                        Log.d("ERROR","ERROR")
                    }
                }
            }
            .addOnFailureListener{
                Log.d("ERROR","ERROR")
            }
    }


    suspend fun getChatFromUser(userUID: String): List<ChatPreview> {
        val previews = mutableListOf<ChatPreview>()

        try {
            val chatSnapshot = firestore.collection("chats")
                .whereArrayContains("participants", userUID)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            for (doc in chatSnapshot.documents) {
                val chat = doc.toObject(Chat::class.java)?.copy(id = doc.id) ?: continue

                val otherUserId = chat.participants.first { it != userUID }

                val userDoc = firestore.collection("users").document(otherUserId).get().await()
                val user = userDoc.toObject(User::class.java)

                if (user != null) {
                    Log.d("SUCCESS","SUCCESS")
                    previews.add(
                        ChatPreview(
                            chatId = chat.id,
                            otherUserName = user.fullname,
                            otherUserImageUrl = user.profileImage,
                            lastMessage = CryptoUtils.decrypt(chat.lastMessage),
                            timestamp = chat.lastMessageTimestamp
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Errror no chats data", e)
        }

        return previews
    }
    suspend fun getMessages(
        chatId: String,
        onSuccess:(List<Message>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val messages = snapshot.toObjects(Message::class.java)
                onSuccess(messages)
            }
            .addOnFailureListener{ e ->
                onFailure(e)
            }
    }
    fun sendMessage(
        chatId: String,
        message: Message,
    ) {
        val chatRef = firestore.collection("chats").document(chatId)
        val newMessageRef = chatRef.collection("messages").document()

        firestore.runBatch { batch ->
            batch.set(newMessageRef, message)
            batch.update(chatRef, mapOf(
                "lastMessage" to message.text,
                "lastMessageTimestamp" to message.timestamp
            ))
        }.addOnSuccessListener {
            Log.d("GOD","SUCCESS")
        }
            .addOnFailureListener{
                Log.d("ERROR","ERROR")
            }
    }
    fun observeMessages(
        chatId: String,
        onChange: (List<Message>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java) } ?: emptyList()
                onChange(messages)
            }
    }
    fun createFine(fine: Fine): Result<Unit>{
        return try {
            val fineRef = firestore.collection("fines").add(fine)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}