package com.example.prova1.ui.screens

import com.example.prova1.data.model.SavingsInput
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SavingsScreen(
    onNavigateToSavingsTable: () -> Unit,
    navController: NavController
) {
    var targetValue by remember { mutableStateOf("") }
    var initialDeposit by remember { mutableStateOf("") }
    var monthlyContribution by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Simulador de Investimentos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = targetValue,
            onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) targetValue = it },
            label = { Text("Valor a atingir (R$)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = initialDeposit,
            onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) initialDeposit = it },
            label = { Text("Depósito Inicial") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = monthlyContribution,
            onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) monthlyContribution = it },
            label = { Text("Aporte Mensal (R$)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = interestRate,
            onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) interestRate = it },
            label = { Text("Rendimento Mensal (%)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val input = SavingsInput(
                        targetValue = targetValue.toDoubleOrNull() ?: 0.0,
                        initialDeposit = initialDeposit.toDoubleOrNull() ?: 0.0,
                        monthlyContribution = monthlyContribution.toDoubleOrNull() ?: 0.0,
                        interestRate = interestRate.toDoubleOrNull() ?: 0.0
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set("savingsInput", input)
                    onNavigateToSavingsTable()
                },
                modifier = Modifier.weight(1f),
                enabled = targetValue.isNotEmpty() &&
                        initialDeposit.isNotEmpty() &&
                        monthlyContribution.isNotEmpty() &&
                        interestRate.isNotEmpty()
            ) {
                Text("CALCULAR")
            }

            Button(
                onClick = {
                    targetValue = ""
                    initialDeposit = ""
                    monthlyContribution = ""
                    interestRate = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("LIMPAR")
            }
        }
    }
}