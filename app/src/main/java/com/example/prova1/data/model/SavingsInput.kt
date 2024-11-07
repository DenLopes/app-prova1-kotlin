package com.example.prova1.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavingsInput(
    val targetValue: Double,
    val initialDeposit: Double,
    val monthlyContribution: Double,
    val interestRate: Double
) : Parcelable