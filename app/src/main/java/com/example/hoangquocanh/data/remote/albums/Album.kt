package com.example.hoangquocanh.data.remote.albums

import com.google.gson.annotations.SerializedName

data class Album (

    @SerializedName("name"      ) var name      : String?          = null,
    @SerializedName("playcount" ) var playcount : Int?             = null,
    @SerializedName("url"       ) var url       : String?          = null,
    @SerializedName("artist"    ) var artist    : Artist?          = Artist(),
    @SerializedName("image"     ) var image     : ArrayList<Image> = arrayListOf()

)