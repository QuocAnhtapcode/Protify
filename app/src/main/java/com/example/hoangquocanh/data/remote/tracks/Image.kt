package com.example.hoangquocanh.data.remote.tracks

import com.google.gson.annotations.SerializedName


data class Image (

    @SerializedName("#text" ) var text : String? = null,
    @SerializedName("size"  ) var size  : String? = null

)