package com.flickry.android.presentation.feed.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flickry.android.R
import com.flickry.android.databinding.ActivityDetailBinding
import com.flickry.android.presentation.base.ImageLoader.load
import com.flickry.android.presentation.feed.main.EXTRA_FEED_ITEM
import com.flickry.android.presentation.feed.main.FeedUiMapper


class FeedDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras

        bundle?.get(EXTRA_FEED_ITEM)?.let {
            if (it is FeedUiMapper.FeedItemUi) {
                binding.detailTitle.text = it.title
                binding.detailImage.contentDescription = it.description
                binding.detailImage.load(
                    url = it.imageUrl,
                    placeholderIcon = it.placeholderImage
                )
                binding.author.text = resources.getString(R.string.detail_author, it.author)
                binding.closeButton.setOnClickListener { finish() }
            } else {
                finish()
            }
        }
    }
}