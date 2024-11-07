package com.example.prova1.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prova1.ui.components.AppHeader
import com.example.prova1.ui.screens.HomeScreen
import com.example.prova1.ui.screens.IMCCalculatorScreen
import com.example.prova1.ui.screens.SavingsScreen
import com.example.prova1.ui.screens.SavingsScreenTable

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Menu Principal")
    data object IMCCalculator : Screen("imc", "Calculadora IMC")
    data object Savings : Screen("savings", "Calculadora de Rendimento")
    data object SavingsTable : Screen("savingsTable", "Tabela de Rendimento")
}

@Composable
fun MainLayout(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentTitle = when (currentRoute) {
        Screen.Home.route -> Screen.Home.title
        Screen.IMCCalculator.route -> Screen.IMCCalculator.title
        Screen.Savings.route -> Screen.Savings.title
        Screen.SavingsTable.route -> Screen.SavingsTable.title
        else -> Screen.Home.title
    }

    val showBackButton = currentRoute != Screen.Home.route

    Column {
        AppHeader(
            title = currentTitle,
            navController = navController,
            showBackButton = showBackButton
        )

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToIMC = { navController.navigate(Screen.IMCCalculator.route) },
                    onNavigateToSavings = { navController.navigate(Screen.Savings.route) }
                )
            }

            composable(Screen.IMCCalculator.route) {
                IMCCalculatorScreen()
            }

            composable(Screen.Savings.route) {
                SavingsScreen(
                    onNavigateToSavingsTable = { navController.navigate(Screen.SavingsTable.route) },
                    navController
                )
            }

            composable(Screen.SavingsTable.route){
                SavingsScreenTable(
                    navController
                )
            }
        }
    }
}