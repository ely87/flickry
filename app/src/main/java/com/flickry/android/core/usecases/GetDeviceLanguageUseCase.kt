package com.flickry.android.core.usecases

import java.util.*

class GetDeviceLanguageUseCase {

    operator fun invoke(): String = Locale.getDefault().toLanguageTag()
}