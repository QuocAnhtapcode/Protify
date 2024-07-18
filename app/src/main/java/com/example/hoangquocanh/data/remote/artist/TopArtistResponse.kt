package com.example.hoangquocanh.data.remote.artist

import com.google.gson.annotations.SerializedName


data class TopArtistResponse (

    @SerializedName("artists" ) var artists : Artists? = Artists()

)