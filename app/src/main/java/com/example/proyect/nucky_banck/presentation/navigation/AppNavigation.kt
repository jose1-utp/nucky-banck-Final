package com.example.proyect.nucky_banck.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyect.nucky_banck.presentation.historial.HistorialView
import com.example.proyect.nucky_banck.presentation.home.HomeView
import com.example.proyect.nucky_banck.presentation.login.LoginView
import com.example.proyect.nucky_banck.presentation.register.RegisterView
import com.example.proyect.nucky_banck.presentation.transfer.TransferView

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = "login"
    ) {

        composable("login") {
            BackHandler(enabled = true) { }
            LoginView(navController = navController)
        }

        composable("home/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            BackHandler(enabled = true) { }
            HomeView(
                cedula        = cedula,
                navController = navController,
                onLogout      = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterView(navController = navController)
        }

        composable("transfer/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            TransferView(
                cedula        = cedula,
                navController = navController
            )
        }

        composable("historial/{cedula}") { backStackEntry ->
            val cedula = backStackEntry.arguments?.getString("cedula") ?: ""
            HistorialView(
                cedula        = cedula,
                navController = navController
            )
        }
    }
}
