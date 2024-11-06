package com.example.prova1.ui.screens

import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prova1.data.model.IMCResultado
import com.example.prova1.R

@Composable
fun IMCCalculatorScreen() {
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("masculino") }
    var resultado by remember { mutableStateOf<IMCResultado?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.titulo_imc), modifier = Modifier.padding(1.dp))

        Text(text = stringResource(R.string.instrucao_imc), modifier = Modifier.padding(1.dp))

        // Campo de Peso
        OutlinedTextField(
            value = peso,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    peso = it
                }
            },
            label = { Text(stringResource(R.string.peso_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            isError = peso.isNotEmpty() && peso.toFloatOrNull() == null
        )

        // Campo de Altura
        OutlinedTextField(
            value = altura,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    altura = it
                }
            },
            label = { Text(stringResource(R.string.altura_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            isError = altura.isNotEmpty() && altura.toFloatOrNull() == null
        )

        // Seleção de Gênero
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
            ){
                RadioButton(
                    selected = genero == "masculino",
                    onClick = { genero = "masculino" }
                )
                Text(
                    text = stringResource(R.string.masculino),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
            ) {
                RadioButton(
                    selected = genero == "feminino",
                    onClick = { genero = "feminino" }
                )
                Text(
                    text = stringResource(R.string.feminino),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Calcular Button
            Button(
                onClick = {
                    calcularIMC(
                        peso = peso,
                        altura = altura,
                        genero = genero,
                        onSuccess = { result ->
                            resultado = result
                        },
                        onError = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                enabled = peso.isNotEmpty() && altura.isNotEmpty()
            ) {
                Text(stringResource(R.string.calcular))
            }

            // Limpar Button
            Button(
                onClick = {
                    peso = ""
                    altura = ""
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                enabled = peso.isNotEmpty() || altura.isNotEmpty()
            ) {
                Text(stringResource(R.string.limpar))
            }
        }


        // Resultado
        resultado?.let { result ->
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.resultado_imc,
                        result.imc,
                        result.classificacao
                    )
                )
                TextButton(
                    onClick = { showDialog = true }
                ) {
                    Text(stringResource(R.string.ver_peso_ideal))
                }
            }
        }

        // Dialog do Peso Ideal
        if (showDialog && resultado != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(R.string.peso_ideal_titulo)) },
                text = {
                    resultado?.let { result ->
                        Text(
                            stringResource(
                                R.string.peso_ideal_mensagem,
                                result.pesoMinimo ?: 0f,
                                result.pesoMaximo ?: 0f
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            )
        }
    }
}

fun calcularIMC(
    peso: String,
    altura: String,
    genero: String,
    onSuccess: (IMCResultado) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val pesoValue = peso.toFloatOrNull()
        val alturaValue = altura.toFloatOrNull()

        if (pesoValue == null || alturaValue == null) {
            onError("Por favor, preencha todos os campos corretamente")
            return
        }

        if (alturaValue <= 0 || pesoValue <= 0) {
            onError("Os valores devem ser maiores que zero")
            return
        }

        val imc = pesoValue / (alturaValue * alturaValue)
        val alturaEmCm = alturaValue * 100

        val (classificacao, pesoMinimo, pesoMaximo) = when (genero) {
            "masculino" -> when {
                imc < 20.7 -> Triple("Abaixo do peso", 20.7f * alturaValue * alturaValue, 26.4f * alturaValue * alturaValue)
                imc <= 26.4 -> Triple("Peso ideal", 20.7f * alturaValue * alturaValue, 26.4f * alturaValue * alturaValue)
                imc <= 27.8 -> Triple("Pouco acima do peso", 20.7f * alturaValue * alturaValue, 26.4f * alturaValue * alturaValue)
                imc <= 31.1 -> Triple("Acima do peso", 20.7f * alturaValue * alturaValue, 26.4f * alturaValue * alturaValue)
                else -> Triple("Obesidade", 20.7f * alturaValue * alturaValue, 26.4f * alturaValue * alturaValue)
            }
            else -> when {
                imc < 19.1 -> Triple("Abaixo do peso", 19.1f * alturaValue * alturaValue, 25.8f * alturaValue * alturaValue)
                imc <= 25.8 -> Triple("Peso ideal", 19.1f * alturaValue * alturaValue, 25.8f * alturaValue * alturaValue)
                imc <= 27.3 -> Triple("Pouco acima do peso", 19.1f * alturaValue * alturaValue, 25.8f * alturaValue * alturaValue)
                imc <= 32.3 -> Triple("Acima do peso", 19.1f * alturaValue * alturaValue, 25.8f * alturaValue * alturaValue)
                else -> Triple("Obesidade", 19.1f * alturaValue * alturaValue, 25.8f * alturaValue * alturaValue)
            }
        }

        onSuccess(IMCResultado(imc, classificacao, pesoMinimo, pesoMaximo))
    } catch (e: Exception) {
        onError("Erro ao calcular IMC")
    }
}