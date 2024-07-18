package com.example.hoangquocanh.data.remote.albums
import com.google.gson.annotations.SerializedName


data class TopAlbums (

    @SerializedName("album" ) var album : ArrayList<Album> = arrayListOf(),
    @SerializedName("@attr" ) var attr : attr?           = attr()

)