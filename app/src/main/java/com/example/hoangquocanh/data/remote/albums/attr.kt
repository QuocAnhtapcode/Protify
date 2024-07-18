package com.example.hoangquocanh.data.remote.albums

import com.google.gson.annotations.SerializedName

data class attr (

@SerializedName("artist"     ) var artist     : String? = null,
@SerializedName("page"       ) var page       : String? = null,
@SerializedName("perPage"    ) var perPage    : String? = null,
@SerializedName("totalPages" ) var totalPages : String? = null,
@SerializedName("total"      ) var total      : String? = null

)