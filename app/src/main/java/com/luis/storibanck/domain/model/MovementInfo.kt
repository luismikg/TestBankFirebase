package com.luis.storibanck.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize

@Parcelize
data class MovementInfo(
    var name: String = String(),
    var total: String = String(),
    var uri: String = String(),
    var loc: String = String(),
    var order: Long = 0,
    var type: Long = 0
) : Parcelable
