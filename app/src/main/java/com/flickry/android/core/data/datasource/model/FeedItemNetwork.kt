package com.flickry.android.core.data.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model received from the API for specific feed items.
 * - Expose indicates that the properties should be exposed for JSON serialization
 * and deserialization.
 */
data class FeedItemNetwork(
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("link")
    @Expose
    val link: String,
    @SerializedName("media")
    @Expose
    val media: MediaItemNetwork,
    @SerializedName("date_taken")
    @Expose
    val date_taken: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("published")
    @Expose
    val published: String,
    @SerializedName("author")
    @Expose
    val author: String,
    @SerializedName("author_id")
    @Expose
    val author_id: String,
    @SerializedName("tags")
    @Expose
    val tags: String?
)

data class MediaItemNetwork(
    @SerializedName("m")
    @Expose
    val link: String,
)