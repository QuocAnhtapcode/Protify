package com.example.hoangquocanh.data.local.song

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongDao {
    @Insert
    fun addSong(song: Song)

    @Query("DELETE FROM song")
    fun deleteAll()

    @Query("SELECT * FROM song")
    fun getAllSong(): LiveData<List<Song>>

    @Query("SELECT * FROM song WHERE id = :id")
    fun getSongById(id: Long): Song

    @Query("SELECT COUNT(*) FROM song WHERE path = :path")
    suspend fun countSongsByPath(path: String): Int
}