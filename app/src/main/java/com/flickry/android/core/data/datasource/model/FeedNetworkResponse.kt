package com.flickry.android.core.data.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model received from the API
 */
data class FeedNetworkResponse(
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("items") @Expose
    val items: List<FeedItemNetwork> = emptyList(),
    val nextPage: Int? = null
)