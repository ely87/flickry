package com.flickry.android.presentation.feed.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE
import com.flickry.android.databinding.ActivityMainBinding
import com.flickry.android.presentation.feed.detail.FeedDetailActivity
import com.flickry.android.presentation.feed.main.FeedUiMapper.FeedItemUi
import com.flickry.android.presentation.feed.main.FeedUiMapper.FeedUiProperties
import com.flickry.android.presentation.feed.main.adapter.FeedAdapter
import com.flickry.android.presentation.feed.main.adapter.FeedAdapterItem
import com.flickry.android.presentation.feed.main.adapter.TagItem
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

internal const val EXTRA_FEED_ITEM = "com.flickry.android.extras.feed.MESSAGE"

@AndroidEntryPoint
class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FeedViewModel by viewModels()
    private val listAdapter = FeedAdapter()
    private val tagAdapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        setupClicks()
        setupAdapter()
    }

    private fun initViewModel() {
        viewModel.viewState.asLiveData().observe(this, { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    handleVisibility(uiProperties = uiState.uiProperties)
                }
                is UiState.Loaded -> {
                    handleVisibility(uiProperties = uiState.uiProperties)
                    handleItemsLoaded(uiState.uiProperties as FeedUiProperties.Loaded)
                }
                is UiState.NoResults -> {
                    handleVisibility(uiProperties = uiState.uiProperties)
                    handleNoResults(uiState.uiProperties as FeedUiProperties.NoResult)
                }
            }
        })

        viewModel.events.asLiveData().observe(this, { event ->
            when (event) {
                is ViewEvent.ShowImageFullScreen -> {
                    openDetailScreen(event.feedItem)
                }
            }
        })
    }

    private fun setupClicks() {
        listAdapter.setOnItemClickListener { item, _ ->
            lifecycleScope.launch {
                if (item is FeedAdapterItem) {
                    viewModel.actionStream.emit(Action.ClickItem(item.feedItem))
                }
            }
        }

        tagAdapter.setOnItemClickListener { item, _ ->
            binding.feedSearchInput.setQuery((item as TagItem).tagText, false)
            searchTriggered(item.tagText)
        }

        binding.feedSearchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.feedSearchInput.clearFocus()
                searchTriggered(query.orEmpty())
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun setupAdapter() {
        // List of feed items (gallery)
        binding.list.adapter = listAdapter
        binding.list.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL).apply {
            gapStrategy = GAP_HANDLING_NONE
        }

        // List of popular tags
        binding.popularTags.adapter = tagAdapter
        binding.popularTags.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun handleVisibility(uiProperties: FeedUiProperties) {
        binding.progressLoading.isVisible = uiProperties.loadingVisible
        binding.list.isVisible = uiProperties.feedListVisible
        binding.popularTags.isVisible = uiProperties.popularTagsVisible
    }

    private fun handleItemsLoaded(uiProperties: FeedUiProperties.Loaded) {
        binding.progressLoading.isVisible = false
        listAdapter.addFeedItems(uiProperties.feedItems)
        if (!uiProperties.tags.isNullOrEmpty()) {
            handleTagsLoaded(uiProperties.tags)
        }
    }

    private fun handleTagsLoaded(items: List<String>) {
        tagAdapter.clear()
        binding.progressLoading.isVisible = false
        tagAdapter.addAll(items.map { TagItem(it) })
    }

    private fun handleNoResults(uiProperties: FeedUiProperties.NoResult) {
        Toast.makeText(this, resources.getText(uiProperties.message), Toast.LENGTH_SHORT).show()
    }

    private fun searchTriggered(text: String) {
        lifecycleScope.launch {
            viewModel.actionStream.emit(Action.Search(tags = text))
        }
    }

    private fun openDetailScreen(feedItem: FeedItemUi) {
        startActivity(Intent(this, FeedDetailActivity::class.java).apply {
            putExtra(EXTRA_FEED_ITEM, feedItem)
        })
    }
}