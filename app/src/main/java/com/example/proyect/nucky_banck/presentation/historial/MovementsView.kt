package com.example.proyect.nucky_banck.presentation.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.ui.theme.DeepBlue
import com.example.proyect.nucky_banck.ui.theme.Emerald
import com.example.proyect.nucky_banck.ui.theme.NavyBlue
import com.example.proyect.nucky_banck.ui.theme.TextDark
import com.example.proyect.nucky_banck.ui.theme.TextGray
import com.example.proyect.nucky_banck.ui.theme.White

@Composable
fun HistorialView(
    cedula: String,
    navController: NavController,
    viewModel: MovementsViewModel = viewModel()
) {

    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.cargarHistorial(cedula)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(NavyBlue, DeepBlue))
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, start = 8.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint               = White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Historial",
                        color = White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tus transferencias realizadas",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 28.dp, vertical = 36.dp)
                ) {

                    Text(
                        text = "Movimientos",
                        color = TextDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (movimientos.isEmpty()) {
                        Text(
                            text = "No hay movimientos registrados",
                            color = TextGray,
                            fontSize = 14.sp
                        )
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(movimientos) { mov ->
                                ItemMovimiento(mov)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemMovimiento(mov: Movimiento) {

    val esEnviado = mov.tipo == "enviado"

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = if (esEnviado) NavyBlue.copy(alpha = 0.05f) else Emerald.copy(alpha = 0.07f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = if (esEnviado) "Enviado a" else "Recibido de",
                    color      = TextGray,
                    fontSize   = 12.sp
                )
                Text(
                    text       = mov.nombre,
                    color      = TextDark,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text     = "Cedula: ${mov.cedula}",
                    color    = TextGray,
                    fontSize = 12.sp
                )
                Text(
                    text     = mov.fecha,
                    color    = TextGray,
                    fontSize = 11.sp
                )
            }

            Text(
                text       = "${if (esEnviado) "-" else "+"} ${"$%,.2f".format(mov.monto)}",
                color      = if (esEnviado) NavyBlue else Emerald,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
