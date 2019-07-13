package com.pericle.guessthecar.levels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Level(
    val name: String,
    val imgResource: Int,
    val progress: Int
) : Parcelable