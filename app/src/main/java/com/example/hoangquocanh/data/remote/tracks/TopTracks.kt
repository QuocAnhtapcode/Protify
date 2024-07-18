package com.example.hoangquocanh.data.remote.tracks

import com.google.gson.annotations.SerializedName


data class TopTracks (

    @SerializedName("track" ) var track : ArrayList<Track> = arrayListOf(),
    @SerializedName("@attr" ) var attr : attr?           = attr()

)