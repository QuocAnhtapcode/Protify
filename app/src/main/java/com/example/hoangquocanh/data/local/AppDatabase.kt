package com.example.hoangquocanh.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hoangquocanh.data.local.connection.Connection
import com.example.hoangquocanh.data.local.connection.ConnectionDao
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.playlist.PlaylistDao
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.local.song.SongDao
import com.example.hoangquocanh.data.local.user.User
import com.example.hoangquocanh.data.local.user.UserDao

@Database(entities = [User::class, Playlist::class, Song::class, Connection::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDao
    abstract fun songDAO(): SongDao
    abstract fun playlistDAO(): PlaylistDao
    abstract fun connectionDAO(): ConnectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
