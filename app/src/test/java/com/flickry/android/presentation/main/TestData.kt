package com.flickry.android.presentation.main

import com.flickry.android.core.data.datasource.model.FeedItemNetwork
import com.flickry.android.core.data.datasource.model.MediaItemNetwork
import com.flickry.android.core.domain.FeedItem

object TestData {

    fun getHappyList(): List<FeedItem> = listOf(
        FeedItem(
            title = "t1",
            link = "l1",
            media = "m1",
            description = "d1",
            published = "p1",
            author = "a1",
            tags = "t1,t2"
        ),
        FeedItem(
            title = "t2",
            link = "l2",
            media = "m2",
            description = "d2",
            published = "p2",
            author = "a2",
            tags = "t3,t9"
        ),
        FeedItem(
            title = "t3",
            link = "l3",
            media = "m3",
            description = "d3",
            published = "p3",
            author = "a3",
            tags = "t4,t9"
        ),
        FeedItem(
            title = "t4",
            link = "l4",
            media = "m4",
            description = "d4",
            published = "p4",
            author = "a4",
            tags = "t5,t9"
        ),
        FeedItem(
            title = "t5",
            link = "l5",
            media = "m5",
            description = "d5",
            published = "p5",
            author = "a5",
            tags = "t6,t9"
        )
    )

    fun getListNoTags(): List<FeedItem> = listOf(
        FeedItem(
            title = "t1",
            link = "l1",
            media = "m1",
            description = "d1",
            published = "p1",
            author = "a1",
            tags = ""
        ),
        FeedItem(
            title = "t2",
            link = "l2",
            media = "m2",
            description = "d2",
            published = "p2",
            author = "a2",
            tags = ""
        ),
        FeedItem(
            title = "t3",
            link = "l3",
            media = "m3",
            description = "d3",
            published = "p3",
            author = "a3",
            tags = ""
        ),
        FeedItem(
            title = "t4",
            link = "l4",
            media = "m4",
            description = "d4",
            published = "p4",
            author = "a4",
            tags = ""
        ),
        FeedItem(
            title = "t5",
            link = "l5",
            media = "m5",
            description = "d5",
            published = "p5",
            author = "a5",
            tags = "t5"
        )
    )

    fun getHappyListNetWork() = getHappyList().map {
        FeedItemNetwork(
            title = it.title,
            link = it.link,
            media = MediaItemNetwork(link = it.media),
            date_taken = it.published,
            description = it.description,
            published = it.published,
            author = it.author,
            author_id = it.author,
            tags = it.tags
        )
    }

}