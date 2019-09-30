package com.redbeanlatte11.factchecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redbeanlatte11.factchecker.domain.SignInUseCase
import com.redbeanlatte11.factchecker.domain.ReportVideoUseCase
import com.redbeanlatte11.factchecker.home.GoogleAccountViewModel
import com.redbeanlatte11.factchecker.home.HomeViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val videosRepository: VideosRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(VideosViewModel::class.java) ->
                    VideosViewModel()

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(
                        ReportVideoUseCase()
                    )

                isAssignableFrom(GoogleAccountViewModel::class.java) ->
                    GoogleAccountViewModel(
                        SignInUseCase()
                    )

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}