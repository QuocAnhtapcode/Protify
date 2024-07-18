package com.example.hoangquocanh.playlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hoangquocanh.MyApplication
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.playlist.PlaylistRepository
import com.example.hoangquocanh.data.local.song.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewmodel(application: Application) : AndroidViewModel(application) {
    private val _playlistList = MutableLiveData<List<Playlist>>()
    val playlistList: LiveData<List<Playlist>> get() = _playlistList
    private val repository: PlaylistRepository

    init {
        val playlistDAO = (application as MyApplication).database.playlistDAO()
        repository = PlaylistRepository(playlistDAO)
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getAllPlaylists().observeForever { playlists ->
                _playlistList.postValue(playlists)
            }
        }
    }
    internal fun getSongByPlaylistId(id: Long): LiveData<List<Song>> {
        return repository.getSongByPlaylistId(id)
    }
    internal fun addPlaylist(newPlaylist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlaylist(newPlaylist)
            loadPlaylists()
        }
    }
    internal fun deletePlaylist(oldPlaylist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlaylistById(oldPlaylist.id)
        }
    }
    internal fun updatePlaylist(newPlaylist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePlaylist(newPlaylist)
            loadPlaylists()
        }
    }
}
