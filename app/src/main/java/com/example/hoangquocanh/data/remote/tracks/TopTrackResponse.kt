package com.example.hoangquocanh.data.remote.tracks

import com.google.gson.annotations.SerializedName

data class TopTrackResponse (

    @SerializedName("toptracks" ) var toptracks : TopTracks? = TopTracks()

)