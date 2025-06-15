package com.pmgdev.pulse.repository.firebaserepository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.pmgdev.pulse.repository.model.Chat
import com.pmgdev.pulse.repository.model.ChatPreview
import com.pmgdev.pulse.repository.model.Fine
import com.pmgdev.pulse.repository.model.Message
import com.pmgdev.pulse.repository.model.User
import com.pmgdev.pulse.utils.CryptoUtils
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) {
    /**
     *
     * createChatIfNotExists
     *
     * Crea un chat si no existe. Registra a los participantes y pasa el uid mediante un callback
     *
     * @param userId1
     * @param userId2
     * @param onComplete
     *
     */
    fun createChatIfNotExists(
        userId1: String,
        userId2: String,
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
                    )
                    db.runBatch { batch ->
                        batch.set(newChatRef, chat)
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

    /**
     *
     * getChatFromUser
     *
     * Recoge los chats de un usuario.
     *
     * @param userUID
     *
     */
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
                    val privatekey = CryptoUtils.getKEYRSA(context, auth.currentUser?.uid ?: "")
                    previews.add(
                        ChatPreview(
                            chatId = chat.id,
                            otherUserName = user.fullname,
                            otherUserImageUrl = user.profileImage,
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


    /**
     *
     * getMessages
     *
     * Recoge los mensajes de un chat. Los pasa por un callback.
     *
     * @param chatId
     * @param onSuccess
     * @param onFailure
     *
     */
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

    /**
     *
     * sendMessage
     *
     * Envia un mensaje a un chat mediante una transacción
     *
     * @param chatId
     * @param message
     *
     */
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
                "lastMessageTimestamp" to message.timestamp,
                "lastMessageSender" to message.senderId
            ))
        }.addOnSuccessListener {
            Log.d("GOD","SUCCESS")
        }
            .addOnFailureListener{
                Log.d("ERROR","ERROR")
            }
    }

    /**
     *
     * observeMessages
     *
     * Observa los cambios en los mensajes de un chat. Los pasa por un callback.
     *
     * @param chatId
     * @param onChange
     * @param onError
     *
     */
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

    /**
     *
     * createFine
     *
     * Crea una denuncia del chat.
     *
     * @param fine
     *
     */
    fun createFine(fine: Fine): Result<Unit>{
        return try {
            val fineRef = firestore.collection("fines").add(fine)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     * getUidOtherParticipant
     *
     * Recoge el uid del otro participante de un chat.
     *
     * @param chatId
     *
     */

    suspend fun getUidOtherParticipant(chatId:String): String?{
        val chatRef = firestore.collection("chats").document(chatId).get().await()
        val chat = chatRef.toObject(Chat::class.java)
        for(id in chat?.participants!!){
            if(id != auth.currentUser?.uid){
                return id
            }
        }
        return null
    }
}