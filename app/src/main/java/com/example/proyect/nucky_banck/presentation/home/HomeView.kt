package com.example.proyect.nucky_banck.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import com.example.proyect.nucky_banck.ui.theme.BorderGray
import com.example.proyect.nucky_banck.ui.theme.DeepBlue
import com.example.proyect.nucky_banck.ui.theme.Emerald
import com.example.proyect.nucky_banck.ui.theme.NavyBlue
import com.example.proyect.nucky_banck.ui.theme.TextDark
import com.example.proyect.nucky_banck.ui.theme.TextGray
import com.example.proyect.nucky_banck.ui.theme.White

@Composable
fun HomeView(
    cedula: String,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadUserData(cedula)
        viewModel.cargarMovimientos(cedula)
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

            TopBar(
                nombre   = uiState.fullName,
                onLogout = onLogout
            )

            HomeCard(
                saldo         = uiState.saldo,
                cedula        = cedula,
                movimientos   = movimientos,
                navController = navController
            )
        }
    }
}

@Composable
private fun TopBar(nombre: String, onLogout: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 56.dp, start = 24.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text     = "Bienvenido de nuevo",
                color    = White.copy(alpha = 0.75f),
                fontSize = 14.sp
            )
            Text(
                text       = nombre,
                color      = White,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(onClick = onLogout) {
            Icon(
                imageVector        = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint               = White
            )
        }
    }
}

@Composable
private fun HomeCard(
    saldo: Double,
    cedula: String,
    movimientos: List<Movimiento>,
    navController: NavController
) {

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        colors    = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 36.dp)
        ) {

            Text(text = "Saldo disponible", color = TextGray, fontSize = 14.sp)

            Text(
                text       = "$ %,.2f".format(saldo),
                color      = NavyBlue,
                fontSize   = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Emerald.copy(alpha = 0.12f)
            ) {
                Text(
                    text       = "  Cuenta de ahorros  ",
                    color      = Emerald,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier   = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(color = BorderGray)
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text       = "Acciones rápidas",
                color      = TextDark,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ActionButton(
                    label    = "Transferir",
                    modifier = Modifier.weight(1f),
                    onClick  = { navController.navigate("transfer/$cedula") }
                )

                ActionButton(
                    label    = "Historial",
                    modifier = Modifier.weight(1f),
                    onClick  = { navController.navigate("historial/$cedula") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text       = "Últimos movimientos",
                color      = TextDark,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (movimientos.isEmpty()) {
                Text(
                    text     = "Sin movimientos recientes",
                    color    = TextGray,
                    fontSize = 14.sp
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    movimientos.take(3).forEach { mov ->
                        val esEnviado = mov.tipo == "enviado"
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text       = if (esEnviado) "Enviado a ${mov.nombre}" else "Recibido de ${mov.nombre}",
                                    color      = TextDark,
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.Medium
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
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick  = onClick,
        modifier = modifier.height(52.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.outlinedButtonColors(contentColor = NavyBlue),
        border   = ButtonDefaults.outlinedButtonBorder.copy()
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
