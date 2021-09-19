package com.flickry.android.core.usecases

import com.flickry.android.core.data.repo.FeedRepoResult
import com.flickry.android.core.data.repo.FeedRepository
import com.flickry.android.presentation.main.TestData.getHappyList
import com.flickry.android.presentation.main.TestData.getHappyListNetWork
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFeedByTagsUseCaseTest {

    private val repo: FeedRepository = mockk()
    private val getDeviceLanguageUseCase: GetDeviceLanguageUseCase = mockk {
        every { this@mockk.invoke() } returns "en-US"
    }

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun `Verify result for response with data`() = runBlockingTest {
        coEvery { repo.getSearchFeedItems(any(), any()) } returns FeedRepoResult.Success(getHappyListNetWork())

        val useCase = GetFeedByTagsUseCase(repo, getDeviceLanguageUseCase, testDispatcher)
        assertEquals(GetFeedByTagsUseCase.Result.Loaded(getHappyList()), useCase.invoke("a").first())
    }

    @Test
    fun `Verify result for response with an error`() = runBlockingTest {
        coEvery { repo.getSearchFeedItems(any(), any()) } throws Exception("Some error")

        val useCase = GetFeedByTagsUseCase(repo, getDeviceLanguageUseCase, testDispatcher)
        assertTrue(useCase.invoke("a").first() is GetFeedByTagsUseCase.Result.Error)
    }


}