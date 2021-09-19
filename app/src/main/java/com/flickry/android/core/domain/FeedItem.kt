package com.flickry.android.core.domain

data class FeedItem(
    val title: String,
    val link: String,
    val media: String,
    val description: String,
    val published: String,
    val author: String,
    val tags: String?
)