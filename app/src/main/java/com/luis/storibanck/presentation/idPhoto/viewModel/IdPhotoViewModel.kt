package com.luis.storibanck.presentation.idPhoto.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.storibanck.domain.useCases.SavePhotoUseCase
import com.luis.storibanck.presentation.idPhoto.states.IdPhotoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdPhotoViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase
) : ViewModel() {


    private var _state = MutableStateFlow<IdPhotoState>(IdPhotoState.Starting)
    val state: StateFlow<IdPhotoState> = _state

    fun savePhoto(uri: Uri?) {
        _state.value = IdPhotoState.Loading
        uri?.let { url ->
            viewModelScope.launch {
                val result = savePhotoUseCase(url)
                result.onSuccess {
                    _state.value = IdPhotoState.Success
                }
                result.onFailure {
                    _state.value = IdPhotoState.Error("Error saving ID")
                }
            }
        }
    }
}