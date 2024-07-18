package com.example.hoangquocanh.data.local.playlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hoangquocanh.playlist.PlaylistViewmodel

class PlaylistViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewmodel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}