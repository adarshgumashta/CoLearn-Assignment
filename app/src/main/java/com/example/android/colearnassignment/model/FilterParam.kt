package com.example.android.colearnassignment.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterParam(
    var selectedColor: String,
    var selectedTone: String,
    var selectedOrientation: String,
    var selectedSort: String
):Parcelable
