package com.flickry.android.presentation.feed.main

import androidx.lifecycle.viewModelScope
import com.flickry.android.core.usecases.GetFeedByTagsUseCase
import com.flickry.android.core.usecases.GetPopularTagsUseCase
import com.flickry.android.di.DispatcherModule
import com.flickry.android.presentation.base.BaseViewModel
import com.flickry.android.presentation.base.UserAction
import com.flickry.android.presentation.feed.main.FeedUiMapper.FeedItemUi
import com.flickry.android.presentation.feed.main.FeedUiMapper.FeedUiProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_SEARCH: String = ""

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedByTags: GetFeedByTagsUseCase,
    private val getPopularTags: GetPopularTagsUseCase,
    private val feedUiMapper: FeedUiMapper,
    @DispatcherModule.IoDispatcher bgDispatcher: CoroutineDispatcher
) : BaseViewModel<Action>(bgDispatcher) {

    private val _viewState = MutableStateFlow<UiState?>(null)
    val viewState: Flow<UiState> = _viewState.filterNotNull()

    private val _events = MutableSharedFlow<ViewEvent>()
    val events: Flow<ViewEvent> = _events

    init {
        actionStream {
            collect { userAction ->
                when (userAction) {
                    is Action.ClickItem -> {
                        _events.emit(ViewEvent.ShowImageFullScreen(userAction.feedItem))
                    }
                    is Action.Search -> {
                        loadFeedElements(tags = userAction.tags, loadTags = false)
                    }
                }
            }
        }
        viewModelScope.launch {
            loadFeedElements(tags = DEFAULT_SEARCH, loadTags = true)
        }
    }

    private suspend fun loadFeedElements(tags: String, loadTags: Boolean) =
        getFeedByTags(tags).onEach {
            when (it) {
                is GetFeedByTagsUseCase.Result.Loading -> UiState.Loading(feedUiMapper.getLoadingState())
                is GetFeedByTagsUseCase.Result.Loaded -> {
                    if (it.items.isNullOrEmpty()) {
                        _viewState.emit(UiState.NoResults(feedUiMapper.getNoResultsState()))
                    } else {
                        val popularTags: List<String>? = if (loadTags) {
                            getPopularTags(it.items)
                        } else null

                        _viewState.emit(
                            UiState.Loaded(
                                feedUiMapper.getLoadedState(
                                    it.items,
                                    popularTags
                                )
                            )
                        )
                    }
                }
                is GetFeedByTagsUseCase.Result.Error -> {
                    _viewState.emit(UiState.NoResults(feedUiMapper.getNoResultsState()))
                }
            }
        }.launchIn(viewModelScope)
}

/**
 * Ui view state
 */
sealed class UiState(open val uiProperties: FeedUiProperties) {
    class Loading(override val uiProperties: FeedUiProperties) : UiState(uiProperties)
    data class Loaded(override val uiProperties: FeedUiProperties) : UiState(uiProperties)
    class NoResults(override val uiProperties: FeedUiProperties) : UiState(uiProperties)
}

/**
 * User Actions
 */
sealed class Action : UserAction {
    data class Search(val tags: String) : Action()
    data class ClickItem(val feedItem: FeedItemUi) : Action()
}

sealed class ViewEvent {
    data class ShowImageFullScreen(val feedItem: FeedItemUi) : ViewEvent()
}