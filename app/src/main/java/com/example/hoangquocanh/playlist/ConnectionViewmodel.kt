package com.example.hoangquocanh.playlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hoangquocanh.MyApplication
import com.example.hoangquocanh.data.local.connection.Connection
import com.example.hoangquocanh.data.local.connection.ConnectionRepository
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.local.song.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConnectionViewmodel(application: Application) : AndroidViewModel(application) {
    private val repository: ConnectionRepository
    private val songRepository : SongRepository

    init {
        val connectionDao = MyApplication.getInstance().database.connectionDAO()
        repository = ConnectionRepository(connectionDao)
        val songDao = MyApplication.getInstance().database.songDAO()
        songRepository = SongRepository(songDao)
    }

    internal fun getSongById(id: Long): Song {
        return runBlocking {
            songRepository.getSongById(id)
        }
    }

    internal fun addConnection(song: Song, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val isExist = songRepository.isSongExistByPath(song.path)
            if (!isExist) {
                songRepository.addSong(song)
            }
            if (!repository.isConnectionExist(song.id, playlistId)) {
                val maxOder = repository.getMaxOrder(playlistId) + 1
                repository.addConnection(
                    Connection(
                        songId = song.id, playlistId = playlistId,
                        order = maxOder
                    )
                )
            }
        }
    }
    internal fun updateOrderConnection(songId: Long, playlistId: Long, order: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addConnection(
                Connection(
                    songId = songId,
                    playlistId = playlistId,
                    order = order
                )
            )
        }

    }

    internal fun updateConnection(connection: Connection) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateConnection(connection)
        }
    }

    internal fun getConnectionByPlaylistId(id: Long): LiveData<List<Connection>> {
        return repository.getConnectionByPlaylistId(id)
    }

    internal fun deleteConnection(songId: Long, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteConnection(songId, playlistId)
        }
    }

    internal fun clearConnectionsByPlaylistId(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearConnectionsByPlaylistId(playlistId)
        }
    }
}