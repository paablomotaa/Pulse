package com.pmgdev.pulse.network

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/**
 *
 * NotificationService
 *
 * Interfaz para la solicitud HTTP a la API de OneSignal.
 *
 */
interface NotificationService {

    /**
     *
     * push Notification
     *
     * Método para enviar notificaciones a los usuarios registrados con la id de OneSignal.
     *
     * @param request
     * @param token
     *
     */
    @POST("notifications")
    fun pushNotification(
        @Body request: NotificationPayload,
        @Header("Authorization") token: String = AUTH_HEADER
    ): Call<ResponseBody>

    companion object {
        private const val AUTH_HEADER = "Basic os_v2_app_vlkxw3jcjzfm3jhzg2sn6ommd62nbiklkynesfvuc276s3zhyc27yf74jmvy3xgofsq6yjno6dknapmnd5mur2hvl4kcdwzpd4a64ii"
    }
}

/**
 *
 * NotificationPayLoad
 *
 * Para crear la solicitud a la API de OneSignal.
 *
 * @param applicationId
 * @param recipientIds
 * @param headings
 * @param contents
 * @param data
 *
 */
data class NotificationPayload(
    @SerializedName("app_id") val applicationId: String,
    @SerializedName("include_player_ids") val recipientIds: List<String>,
    val headings: Map<String, String>,
    val contents: Map<String, String>,
    val data: Map<String, String>? = null
)


/**
 *
 * Notification Client
 *
 * Para crear el servicio y enviarlo desde Hilt.
 *
 *
 */
object NotificationClient {
    fun create(): NotificationService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://onesignal.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(NotificationService::class.java)
    }
}
