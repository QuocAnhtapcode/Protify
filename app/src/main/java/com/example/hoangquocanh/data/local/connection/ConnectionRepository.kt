package com.example.hoangquocanh.data.local.connection

import androidx.lifecycle.LiveData

class ConnectionRepository(private val connectionDao: ConnectionDao) {
    fun getAllConnection(): LiveData<List<Connection>> {
        return connectionDao.getAllConnection()
    }

    fun addConnection(connection: Connection) {
        connectionDao.addConnection(connection)
    }

    fun getConnectionByPlaylistId(id: Long): LiveData<List<Connection>> {
        return connectionDao.findSongByPlaylistId(id)
    }

    fun deleteConnection(songId: Long, playlistId: Long) {
        connectionDao.deleteConnection(songId, playlistId)
    }

    fun clearConnectionsByPlaylistId(playlistId: Long) {
        connectionDao.clearConnectionsByPlaylistId(playlistId)
    }

    fun isConnectionExist(songId: Long, playlistId: Long): Boolean {
        val count = connectionDao.countConnection(songId, playlistId)
        return count > 0
    }

    fun updateConnection(newConnection: Connection) {
        connectionDao.updateConnection(newConnection)
    }

    fun getMaxOrder(playlistId: Long): Int {
        return connectionDao.getMaxOrder(playlistId) ?: 0
    }
}