package com.example.hoangquocanh.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hoangquocanh.data.remote.albums.TopAlbumResponse
import com.example.hoangquocanh.data.remote.api.ApiHome
import com.example.hoangquocanh.data.remote.api.ApiService
import com.example.hoangquocanh.data.remote.artist.TopArtistResponse
import com.example.hoangquocanh.data.remote.tracks.TopTrackResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitBuilder = ApiHome.retrofit.create(ApiService::class.java)
    private val _topAlbums = MutableLiveData<TopAlbumResponse>()
    private val _topTracks = MutableLiveData<TopTrackResponse>()
    private val _topArtist = MutableLiveData<TopArtistResponse>()

    val topAlbums get() = _topAlbums
    val topTracks get() = _topTracks
    val topArtist get() = _topArtist

    init {
        fetchAllData()
    }

    private fun fetchAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val albumsDeferred = async { retrofitBuilder.getTopAlbums() }
                val tracksDeferred = async { retrofitBuilder.getTopTracks() }
                val artistsDeferred = async { retrofitBuilder.getTopArtist() }

                val (albumsResponse, tracksResponse, artistsResponse) =
                    awaitAll(albumsDeferred, tracksDeferred, artistsDeferred)

                _topAlbums.postValue(albumsResponse as TopAlbumResponse?)
                _topTracks.postValue(tracksResponse as TopTrackResponse?)
                _topArtist.postValue(artistsResponse as TopArtistResponse?)

                Log.d("HomeViewModel", "Fetched all data successfully")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching data", e)
            }
        }
    }
}
