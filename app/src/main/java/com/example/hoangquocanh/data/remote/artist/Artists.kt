package com.example.hoangquocanh.data.remote.artist

import com.google.gson.annotations.SerializedName


data class Artists (

    @SerializedName("artist" ) var artist : ArrayList<Artist> = arrayListOf(),
    @SerializedName("@attr"  ) var attr  : attr?            = attr()

)