package com.example.proyect.nucky_banck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyect.nucky_banck.presentation.navigation.AppNavigation
import com.example.proyect.nucky_banck.ui.theme.NuckybanckTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NuckybanckTheme {
                AppNavigation()
            }
        }
    }
}