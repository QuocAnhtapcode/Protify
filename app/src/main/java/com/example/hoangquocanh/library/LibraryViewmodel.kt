package com.example.hoangquocanh.library

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.remote.api.ApiClient
import com.example.hoangquocanh.data.remote.song.SongApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private val retrofitBuilder = ApiClient.retrofit.create(SongApiService::class.java)

    private val _localSongFiles = MutableLiveData<MutableList<Song>>()
    val localMusicFiles get() = _localSongFiles
    private val _remoteSongFiles = MutableLiveData<List<Song>>()
    val remoteMusicFiles get() = _remoteSongFiles


    fun getRemoteMusicFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val songsFromApi = retrofitBuilder.getAllSong()
            val songsWithIds = songsFromApi.mapIndexed { index, song ->
                song.copy(id = (index + 1).toLong(), image = song.image ?: "")
            }
            _remoteSongFiles.postValue(songsWithIds)
        }
    }
    fun getLocalMusicFiles(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
            )
            val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
            val cursor: Cursor? =
                context.contentResolver.query(uri, projection, selection, null, sortOrder)

            val songList = mutableListOf<Song>()
            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val kindColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val musicFileName = it.getString(nameColumn) ?: "Song name"
                    val artistName = it.getString(artistColumn) ?: "Artist name"
                    val kind = it.getString(kindColumn) ?: "Kind"
                    val duration = it.getString(durationColumn) ?: "0"
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                    )
                    val newSong = Song(id, "", musicFileName, artistName,
                        kind, duration.toLong(), contentUri.toString())
                    songList.add(newSong)
                }
            }
            cursor?.close()
            _localSongFiles.postValue(songList)
        }
    }
}