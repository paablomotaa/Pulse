package com.pmgdev.pulse.ui.chat.chatbasic

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.pmgdev.pulse.network.NotificationPayload
import com.pmgdev.pulse.network.NotificationService
import com.pmgdev.pulse.repository.firebaserepository.ChatRepository
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.Fine
import com.pmgdev.pulse.repository.model.Message
import com.pmgdev.pulse.repository.model.TypeFine
import com.pmgdev.pulse.utils.CryptoUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.inject.Inject


/**
 *
 * ViewModel para el chat.
 *
 */
@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val auth: FirebaseAuth,
    private val retrofit: NotificationService,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var text by mutableStateOf("")
        private set
    var messages = mutableStateOf<List<Message>>(emptyList())
        private set
    private var messageListener: ListenerRegistration? =
        null //Para que se pueda cambiar de chat y siga escuchando otros mensajes

    var username by mutableStateOf("")
        private set


    fun onTextChange(txt: String) {
        text = txt
    }

    /**
     *
     * observeMessages
     *
     * Metodo para actualizar los mensajes en tiempo real.
     *
     * @param chatId
     *
     */
    fun observeMessages(chatId: String) {

        messageListener?.remove()
        val privatekey = CryptoUtils.getKEYRSA(context, auth.currentUser?.uid ?: "")

        messageListener = repository.observeMessages(
            chatId = chatId,
            onChange = { msgs ->
                messages.value = msgs.map { msg ->
                    msg.copy(
                        text = try {
                            if(msg.senderId == auth.currentUser?.uid){
                                val claveAES = CryptoUtils.decryptAES(msg.keyAesSender,privatekey)
                                CryptoUtils.decryptMessage(msg.text,claveAES)
                            }
                            else{
                                val claveAES = CryptoUtils.decryptAES(msg.keyAesReceiver,privatekey)
                                CryptoUtils.decryptMessage(msg.text,claveAES)
                            }
                        } catch (e: Exception) {
                            "[Mensaje no válido]"
                        }
                    )
                }
            },
            onError = { e ->
                Log.d("ERROR observeMessages", e.message.toString())
            }
        )
    }



    /**
     *
     * loadMessages
     *
     * Metodo para cargar los mensajes del chat
     *
     * @param chatId
     *
     */
    fun loadMessages(chatId: String) {

        viewModelScope.launch {

            val privatekey = CryptoUtils.getKEYRSA(context, auth.currentUser?.uid ?: "")
                repository.getMessages(
                    chatId = chatId,
                    { msg ->
                        messages.value = msg.map { msg ->

                            msg.copy(
                                text = try {
                                    if(msg.senderId == auth.currentUser?.uid){
                                        val claveAES = CryptoUtils.decryptAES(msg.keyAesSender,privatekey)
                                        CryptoUtils.decryptMessage(msg.text,claveAES)
                                    }
                                    else{
                                        val claveAES = CryptoUtils.decryptAES(msg.keyAesReceiver,privatekey)
                                        CryptoUtils.decryptMessage(msg.text,claveAES)
                                    }

                                } catch (e: Exception) {
                                    "${e.localizedMessage}"
                                }
                            )
                        }
                    },
                    { e ->
                        Log.d("ERROR loadingMessages", e.message.toString())
                    }
                )
        }
    }

    /**
     *
     * sendMessage
     *
     * Metodo para enviar el mensaje. Se cifra la clave AES y el mensaje tanto del que lo envia como el que lo recibe.
     *
     * @param chatId
     *
     */
    fun sendMessage(chatId: String) {


        viewModelScope.launch {
            val currentUser = userRepository.getUser(auth.currentUser?.uid ?: "")

            val uid = repository.getUidOtherParticipant(chatId)
            val user = userRepository.getUser(uid!!)
            val claveAES = CryptoUtils.genAES()
            val messageEncrypted = CryptoUtils.encryptMessage(text, claveAES)


            //Encriptar la clave AES con la clave RSA de mi usuario.
            val publicKeyByte = Base64.decode(currentUser?.publicKey, Base64.NO_WRAP)
            val keySpec = X509EncodedKeySpec(publicKeyByte)
            val publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec)
            val keyAesSender = CryptoUtils.encryptAES(claveAES, publicKey)

            //Encriptar la clave AES con la clave RSA del otro usuario.
            val publicKeyByte2 = Base64.decode(user?.publicKey, Base64.NO_WRAP)
            val keySpec2 = X509EncodedKeySpec(publicKeyByte2)
            val publicKey2 = KeyFactory.getInstance("RSA").generatePublic(keySpec2)
            val keyAesReceiver = CryptoUtils.encryptAES(claveAES, publicKey2)


            val message = Message(
                id = "",
                senderId = auth.currentUser?.uid ?: "",
                text = messageEncrypted,
                timestamp = Timestamp.now(),
                keyAesSender = keyAesSender,
                keyAesReceiver = keyAesReceiver
            )

            repository.sendMessage(
                chatId = chatId,
                message = message,
            )
            if(user != null){
                val notificationRequest = NotificationPayload(
                    applicationId = "aad57b6d-224e-4acd-a4f9-36a4df398c1f",
                    recipientIds = listOf(user.idOneSignal),
                    headings = mapOf(
                        "en" to "Nuevo mensaje de ${currentUser?.username}"
                    ),
                    contents = mapOf("en" to text),
                    data = mapOf("en" to user.idOneSignal)
                )
                // Envio de notificacion utilizando retrofit.
                retrofit.pushNotification(request = notificationRequest).enqueue(object :
                    retrofit2.Callback<ResponseBody> {
                    override fun onFailure(
                        call: Call<ResponseBody>,
                        t: Throwable
                    ) {
                        Log.d("Succes","Succes")
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.d("Succes","Success")
                    }
                }
                )
                text = ""
            }
        }
    }


    /**
     *
     * messageIsFrom
     *
     * Metodo para que la interfaz sepa de quien es el mensaje
     * y lo cambie de lado y de color.
     *
     * @param senderId
     *
     */

    fun messageIsFrom(senderId: String): Boolean {
        val currentUid = auth.currentUser?.uid
        if (currentUid != senderId) {
            return false
        } else {
            return true
        }
    }

    /**
     *
     * createFine
     *
     * Para crear un reporte de un chat.
     *
     */

    fun createFine(chatId: String) {

        repository.createFine(
            Fine(
                userId = auth.currentUser?.uid ?: "",
                reportedId = chatId,
                TypeFine = TypeFine.CHAT
            )
        )
    }

    /**
     *
     * getUsernameFromOtherParticipant
     *
     * Metodo para obtener el nombre del usuario del otro participante del chat.
     *
     * @param chatId
     *
     */
    fun getUsernameFromOtherParticipant(chatId: String) {
        viewModelScope.launch {
            val uid = repository.getUidOtherParticipant(chatId)
            var user = userRepository.getUser(uid.toString())
            if(user != null){
                username = user.fullname
            }
        }
    }
}