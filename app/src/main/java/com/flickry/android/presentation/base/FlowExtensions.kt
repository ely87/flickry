package com.flickry.android.presentation.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

/**
 * Throttles a before emitting for avoiding duplicated emissions (multiple emissions of the same item)
 */
internal fun <T> Flow<T>.throttleFirst(windowDuration: Duration = Duration.milliseconds(500)): Flow<T> =
    flow {
        var windowStartTime = Duration.milliseconds(System.currentTimeMillis())
        var emitted = false
        collect { value ->
            val currentTime = Duration.milliseconds(System.currentTimeMillis())
            val delta = currentTime - windowStartTime
            if (delta >= windowDuration) {
                windowStartTime += delta
                emitted = false
            }
            if (!emitted) {
                emit(value)
                emitted = true
            }
        }
    }