package com.luis.storibanck.presentation.splash.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.luis.storibanck.presentation.splash.states.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private var _state = MutableStateFlow<SplashState>(SplashState.Starting)
    val state: StateFlow<SplashState> = _state

    fun startApplicacionCount() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            _state.value = SplashState.StartApplication
        }, 3000)
    }
}