package com.example.hoangquocanh.data

import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.local.user.User

object Data {
    var currentUser: User? = null
    var isGridView = false
    var listSongRemote: List<Song> = emptyList()
}

