package com.example.hoangquocanh.data.remote.tracks


import com.google.gson.annotations.SerializedName


data class Track (

    @SerializedName("name"       ) var name       : String?          = null,
    @SerializedName("playcount"  ) var playcount  : String?          = null,
    @SerializedName("listeners"  ) var listeners  : String?          = null,
    @SerializedName("url"        ) var url        : String?          = null,
    @SerializedName("streamable" ) var streamable : String?          = null,
    @SerializedName("artist"     ) var artist     : Artist?          = Artist(),
    @SerializedName("image"      ) var image      : ArrayList<Image> = arrayListOf(),
    @SerializedName("@attr"      ) var attr      : attr?           = attr()

)