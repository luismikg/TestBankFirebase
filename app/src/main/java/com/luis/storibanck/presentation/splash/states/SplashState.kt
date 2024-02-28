package com.luis.storibanck.presentation.splash.states

sealed class SplashState {
    data object Starting : SplashState()
    data object StartApplication : SplashState()
}
