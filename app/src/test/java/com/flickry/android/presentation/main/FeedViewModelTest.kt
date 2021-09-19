package com.flickry.android.presentation.main

import app.cash.turbine.test
import com.flickry.android.core.domain.FeedItem
import com.flickry.android.core.usecases.GetFeedByTagsUseCase
import com.flickry.android.core.usecases.GetFeedByTagsUseCase.Result
import com.flickry.android.core.usecases.GetPopularTagsUseCase
import com.flickry.android.presentation.feed.main.Action
import com.flickry.android.presentation.feed.main.FeedUiMapper
import com.flickry.android.presentation.feed.main.FeedViewModel
import com.flickry.android.presentation.feed.main.UiState.Loaded
import com.flickry.android.presentation.feed.main.UiState.NoResults
import com.flickry.android.presentation.feed.main.mapToUi
import com.flickry.android.presentation.main.TestData.getHappyList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class FeedViewModelTest {

    private val getFeedByTagsUseCase: GetFeedByTagsUseCase = mockk()
    private val getPopularTagsUseCase: GetPopularTagsUseCase = mockk()
    private val feedUIMapper: FeedUiMapper = FeedUiMapper()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Verify feed items and tags are loaded when repo returns results`() = runBlockingTest {
        val list = getHappyList()
        val tags = list.map { it.tags.orEmpty() }
        coEvery { getFeedByTagsUseCase.invoke(any()) } returns flowOf(Result.Loaded(list))
        coEvery { getPopularTagsUseCase.invoke(list) } returns tags

        val viewModel = getViewModel()
        viewModel.viewState.test {
            assertEquals(Loaded(feedUIMapper.getLoadedState(list, tags)), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `Verify tags are only loaded once`() = runBlockingTest {
        val list: List<FeedItem> = getHappyList()
        val tags: List<String> = list.map { it.tags.orEmpty() }
        coEvery { getFeedByTagsUseCase.invoke(any()) } returns flowOf(Result.Loaded(list))
        coEvery { getPopularTagsUseCase.invoke(list) } returns tags

        val viewModel = getViewModel()

        viewModel.viewState.test {
            assertEquals(Loaded(feedUIMapper.getLoadedState(list, tags)), awaitItem())

            // Simulate a search action
            viewModel.actionStream.emit(Action.Search("search2"))

            assertEquals(Loaded(feedUIMapper.getLoadedState(list, null)), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `Verify correct ui state when empty list returned from repo on first load`() = runBlockingTest {
        val list: List<FeedItem> = emptyList()
        val listAsUI: List<FeedUiMapper.FeedItemUi> = list.mapToUi()
        val tags: List<String> = list.map { it.tags.orEmpty() }
        coEvery { getFeedByTagsUseCase.invoke(any()) } returns flowOf(Result.Loaded(list))
        coEvery { getPopularTagsUseCase.invoke(list) } returns tags

        val viewModel = getViewModel()
        viewModel.viewState.test {
            awaitItem() is NoResults
            expectNoEvents()
        }
    }

    @Test
    fun `Verify correct ui state when no results are returned from repo on second load`() = runBlockingTest {
        val list: List<FeedItem> = getHappyList()
        val tags: List<String> = list.map { it.tags.orEmpty() }
        coEvery { getFeedByTagsUseCase.invoke(any()) } returnsMany listOf(
            flowOf(Result.Loaded(list)),
            flowOf(Result.Error(Exception()))
        )
        coEvery { getPopularTagsUseCase.invoke(list) } returns tags

        val viewModel = getViewModel()

        viewModel.viewState.test {
            assertEquals(Loaded(feedUIMapper.getLoadedState(list, tags)), awaitItem())

            // Simulate a search action
            viewModel.actionStream.emit(Action.Search("noresultsearch"))

            awaitItem() is NoResults
            expectNoEvents()
        }
    }

    private fun getViewModel(): FeedViewModel =
        FeedViewModel(getFeedByTagsUseCase, getPopularTagsUseCase, feedUIMapper, testDispatcher)
}