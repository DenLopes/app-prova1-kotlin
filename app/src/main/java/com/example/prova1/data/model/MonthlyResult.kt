package com.example.prova1.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonthlyResult(
    val month: Int,
    val interest: Double,
    val deposit: Double,
    val balance: Double
): Parcelable
