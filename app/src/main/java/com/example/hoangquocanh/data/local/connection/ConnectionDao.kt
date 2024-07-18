package com.example.hoangquocanh.data.local.connection

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConnectionDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addConnection(connection: Connection)
    @Query("SELECT * FROM connection ORDER BY `order` ASC")
    fun getAllConnection(): LiveData<List<Connection>>
    @Query("SELECT * FROM connection WHERE songId = :songId ORDER BY `order` ASC")
    fun findPlaylistBySongId(songId: Long): LiveData<List<Connection>>
    @Query("SELECT * FROM connection WHERE playlistId = :playlistId ORDER BY `order` ASC")
    fun findSongByPlaylistId(playlistId: Long): LiveData<List<Connection>>
    @Query("DELETE FROM connection WHERE songId = :songId AND playlistId = :playlistId")
    fun deleteConnection(songId: Long, playlistId: Long)
    @Query("DELETE FROM connection WHERE playlistId = :playlistId")
    fun clearConnectionsByPlaylistId(playlistId: Long)
    @Query("SELECT COUNT(*) FROM connection WHERE songId = :songId AND playlistId = :playlistId")
    fun countConnection(songId: Long, playlistId: Long): Int
    @Update
    fun updateConnection(connection: Connection)

    @Query("SELECT MAX(`order`) FROM connection WHERE playlistId = :playlistId")
    fun getMaxOrder(playlistId: Long): Int?
}