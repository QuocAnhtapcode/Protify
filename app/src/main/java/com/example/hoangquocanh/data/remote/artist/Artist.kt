package com.example.hoangquocanh.data.remote.artist

import com.google.gson.annotations.SerializedName

data class Artist (

    @SerializedName("name"       ) var name       : String?          = null,
    @SerializedName("playcount"  ) var playcount  : String?          = null,
    @SerializedName("listeners"  ) var listeners  : String?          = null,
    @SerializedName("mbid"       ) var mbid       : String?          = null,
    @SerializedName("url"        ) var url        : String?          = null,
    @SerializedName("streamable" ) var streamable : String?          = null,
    @SerializedName("image"      ) var image      : ArrayList<Image> = arrayListOf()

)