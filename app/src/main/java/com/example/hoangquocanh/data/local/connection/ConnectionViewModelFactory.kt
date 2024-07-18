package com.example.hoangquocanh.data.local.connection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hoangquocanh.playlist.ConnectionViewmodel

class ConnectionViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectionViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConnectionViewmodel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
