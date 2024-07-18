package com.example.hoangquocanh.data.local.playlist

import androidx.lifecycle.LiveData
import com.example.hoangquocanh.data.local.song.Song

class PlaylistRepository(private val playlistDao: PlaylistDao) {
    fun getAllPlaylists(): LiveData<List<Playlist>> {
        return playlistDao.getALlPlaylist()
    }
    fun addPlaylist(newPlaylist: Playlist) {
        playlistDao.addPlayList(newPlaylist)
    }
    fun findPlaylistById(id: Long): LiveData<Playlist> {
        return playlistDao.findPlaylistById(id)
    }
    fun getSongByPlaylistId(id: Long): LiveData<List<Song>>{
        return playlistDao.getSongsByPlaylistId(id)
    }
    fun deletePlaylistById(id:Long){
        playlistDao.deleteById(id)
    }
    fun updatePlaylist(newPlaylist: Playlist) {
        playlistDao.updatePlaylist(newPlaylist)
    }
}
