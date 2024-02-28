package com.luis.storibanck.presentation.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.storibanck.domain.model.MovementInfo
import com.luis.storibanck.domain.model.TotalInfo
import com.luis.storibanck.domain.useCases.FetchMovementUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchMovementUserUseCase: FetchMovementUserUseCase
) : ViewModel() {

    private val mutableLiveDataMovements = MutableLiveData<List<MovementInfo>>()
    val liveDataMovements: LiveData<List<MovementInfo>> = mutableLiveDataMovements

    private val mutableLiveDataHead = MutableLiveData<TotalInfo>()
    val liveDataHead: LiveData<TotalInfo> = mutableLiveDataHead

    fun fetchMovement() {
        viewModelScope.launch {
            fetchMovementUserUseCase(mutableLiveDataMovements, mutableLiveDataHead)
        }
    }
}