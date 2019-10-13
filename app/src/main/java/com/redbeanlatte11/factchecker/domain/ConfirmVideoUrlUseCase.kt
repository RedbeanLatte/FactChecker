package com.redbeanlatte11.factchecker.domain

import com.redbeanlatte11.factchecker.data.Result
import com.redbeanlatte11.factchecker.data.Result.Error
import com.redbeanlatte11.factchecker.data.Video

class ConfirmVideoUrlUseCase {

    operator fun invoke(url: String): Result<Video> {
        return Error(IllegalStateException())
    }
}