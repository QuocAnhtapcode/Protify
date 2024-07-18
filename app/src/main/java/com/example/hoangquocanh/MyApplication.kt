package com.example.hoangquocanh

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.hoangquocanh.data.local.AppDatabase

class MyApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    companion object {
        const val CHANNEL_ID = "channel_id"

        @Volatile
        private lateinit var INSTANCE: MyApplication

        @Synchronized
        fun getInstance(): MyApplication {
            if (!this::INSTANCE.isInitialized) {
                INSTANCE = MyApplication()
                return INSTANCE
            }
            return INSTANCE
        }

        fun context(): Context {
            return getInstance().applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        createChannelNotification()
    }

    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(null, null)
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

}