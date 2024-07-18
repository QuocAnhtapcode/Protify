package com.example.hoangquocanh.data.local.song

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongRepository(private val songDao: SongDao) {
    fun getAllSong(): LiveData<List<Song>> {
        return songDao.getAllSong()
    }

    fun deleteAll() {
        songDao.deleteAll()
    }

    suspend fun getSongById(id: Long): Song {
        return withContext(Dispatchers.IO) {
            songDao.getSongById(id)
        }
    }

    suspend fun isSongExistByPath(path: String): Boolean {
        val count = songDao.countSongsByPath(path)
        return count > 0
    }

    fun addSong(song: Song) {
        songDao.addSong(song)
    }
}
fun loadAlbumArt(filePath: String, imageView: ImageView) {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(filePath)
        val art = retriever.embeddedPicture
        if (art != null) {
            val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            Glide.with(imageView)
                .load(bitmap)
                .into(imageView)
        } else {
            Glide.with(imageView)
                .load(R.drawable.default_song_image)
                .into(imageView)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Glide.with(imageView)
            .load(R.drawable.default_song_image)
            .into(imageView)
    } finally {
        retriever.release()
    }
}