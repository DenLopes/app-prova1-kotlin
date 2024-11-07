package com.example.prova1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.prova1.data.model.SavingsInput
import com.example.prova1.data.model.MonthlyResult

@Composable
fun SavingsScreenTable(
    navController: NavController
) {
    val input = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<SavingsInput>(
            "savingsInput"
        )
    }

    if (input == null) {
        Text("Erro ao carregar dados")
        return
    }

    val results = calculateInvestmentProgress(input)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Evolução do Investimento:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Table Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Mês", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text("Juros", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text("Depósito", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text("Reserva", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Results Table
        LazyColumn {
            items(results) { month ->
                MonthlyResultRow(month)
                HorizontalDivider()
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MonthlyResultRow(result: MonthlyResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${result.month}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "%.2f".format(result.interest),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "%.2f".format(result.deposit),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "%.2f".format(result.balance),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

fun calculateInvestmentProgress(input: SavingsInput): List<MonthlyResult> {
    val results = mutableListOf<MonthlyResult>()
    var currentBalance = input.initialDeposit
    var month = 1

    while (currentBalance < input.targetValue) {
        val monthlyInterest = currentBalance * (input.interestRate / 100)
        currentBalance += monthlyInterest + input.monthlyContribution

        results.add(
            MonthlyResult(
                month = month,
                interest = monthlyInterest,
                deposit = input.monthlyContribution,
                balance = currentBalance
            )
        )

        month++
        if (month > 240) break // Fiz isso aqui para sair do loop e não ficar infinito
    }

    return results
}