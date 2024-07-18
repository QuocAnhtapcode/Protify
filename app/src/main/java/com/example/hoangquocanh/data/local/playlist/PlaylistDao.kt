package com.example.hoangquocanh.data.local.playlist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.hoangquocanh.data.local.song.Song

@Dao
interface PlaylistDao{
    @Query("SELECT * FROM playlist")
    fun getALlPlaylist(): LiveData<List<Playlist>>
    @Insert
    fun addPlayList(newPlaylist: Playlist)
    @Query("SELECT * FROM playlist WHERE id == :id")
    fun findPlaylistById(id: Long): LiveData<Playlist>

    @Transaction
    @Query("""
        SELECT song.* FROM song 
        INNER JOIN connection ON song.id = connection.songId 
        WHERE connection.playlistId = :playlistId 
        ORDER BY connection.[order] ASC
    """)
    fun getSongsByPlaylistId(playlistId: Long): LiveData<List<Song>>

    @Query("DELETE FROM playlist WHERE id = :id")
    fun deleteById(id: Long)
    @Update
    fun updatePlaylist(vararg newPlaylist: Playlist)
}