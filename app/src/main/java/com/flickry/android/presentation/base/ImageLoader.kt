package com.flickry.android.presentation.base

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

private const val RADIUS: Int = 8

object ImageLoader {

    fun ImageView.load(
        url: String,
        @DrawableRes placeholderIcon: Int
    ) = Glide.with(this)
        .load(url)
        .dontAnimate()
        .transform(RoundedCorners(RADIUS))
        .placeholder(placeholderIcon)
        .into(this)

}