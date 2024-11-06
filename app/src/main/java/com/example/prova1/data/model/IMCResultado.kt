package com.example.prova1.data.model

data class IMCResultado(
    val imc: Float,
    val classificacao: String,
    val pesoMinimo: Float? = null,
    val pesoMaximo: Float? = null
)
