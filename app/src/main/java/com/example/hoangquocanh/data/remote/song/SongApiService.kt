package com.example.hoangquocanh.data.remote.song

import com.example.hoangquocanh.data.local.song.Song
import retrofit2.http.GET

interface SongApiService {
    @GET("Remote_audio.json")
    suspend fun getAllSong(): List<Song>
}