package com.example.hoangquocanh.data.remote.albums

import com.google.gson.annotations.SerializedName


data class TopAlbumResponse (

    @SerializedName("topalbums" ) var topalbums : TopAlbums? = TopAlbums()

)