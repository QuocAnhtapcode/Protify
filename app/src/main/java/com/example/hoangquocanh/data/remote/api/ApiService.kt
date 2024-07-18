package com.example.hoangquocanh.data.remote.api

import com.example.hoangquocanh.data.remote.albums.TopAlbumResponse
import com.example.hoangquocanh.data.remote.artist.TopArtistResponse
import com.example.hoangquocanh.data.remote.tracks.TopTrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("2.0/")
    suspend fun getTopAlbums(
        @Query("api_key") apiKey: String = "e65449d181214f936368984d4f4d4ae8",
        @Query("format") format: String = "json",
        @Query("method") method: String = "artist.getTopAlbums",
        @Query("mbid") album: String = "f9b593e6-4503-414c-99a0-46595ecd2e23"
    ): TopAlbumResponse
    @GET("2.0/?api_key=e65449d181214f936368984d4f4d4ae8&format=json&method=chart.gettopartists")
    suspend fun getTopArtist(): TopArtistResponse
    @GET("2.0/?api_key=e65449d181214f936368984d4f4d4ae8&format=json&method=artist.getTopTracks&mbid=f9b593e6-4503-414c-99a0-46595ecd2e23")
    suspend fun getTopTracks(): TopTrackResponse
}