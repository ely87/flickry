package com.flickry.android.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
abstract class BaseViewModel<T : UserAction>(private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    ViewModel() {

    val actionStream by lazy { MutableSharedFlow<T>() }

    protected fun actionStream(userAction: suspend Flow<T>.() -> Unit) {
        viewModelScope.launch(bgDispatcher) { actionStream.userAction() }
    }

    protected fun collectThrottledActions(action: (value: T) -> Unit) {
        actionStream {
            throttleFirst().collect {
                action(it)
            }
        }
    }

}

/**
 * A streamed action in the viewmodel from the UI
 */
interface UserAction