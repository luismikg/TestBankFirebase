package com.luis.storibanck.domain.useCases

import androidx.lifecycle.MutableLiveData
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import com.luis.storibanck.domain.model.MovementInfo
import com.luis.storibanck.domain.model.TotalInfo
import javax.inject.Inject

class FetchMovementUserUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    operator fun invoke(
        stateMovement: MutableLiveData<List<MovementInfo>>,
        stateHead: MutableLiveData<TotalInfo>
    ) {
        firebaseAuthRepository.fetchMovement(stateMovement, stateHead)
    }
}