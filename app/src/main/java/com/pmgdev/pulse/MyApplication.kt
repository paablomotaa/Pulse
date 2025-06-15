package com.pmgdev.pulse

import android.app.Application
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.pmgdev.pulse.utils.CryptoUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        OneSignal.initWithContext(this, "aad57b6d-224e-4acd-a4f9-36a4df398c1f")


        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
    }
}