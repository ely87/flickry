package com.flickry.android.core.usecases

import com.flickry.android.presentation.main.TestData
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPopularTagsUseCaseTest {

    @Test
    fun `Verify no empty tags are included in the result`(){
        val useCase = GetPopularTagsUseCase()
        val list = TestData.getListNoTags()
        val expectedResult = list.last().tags

        assertEquals(listOf(expectedResult), useCase(list))
    }
}