package com.example.proyect.nucky_banck.presentation.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyect.nucky_banck.presentation.components.NuckyTextField
import com.example.proyect.nucky_banck.presentation.components.ShowLoadingAlertDialog
import com.example.proyect.nucky_banck.presentation.components.ShowMessageAlertDialog
import com.example.proyect.nucky_banck.ui.theme.DeepBlue
import com.example.proyect.nucky_banck.ui.theme.Emerald
import com.example.proyect.nucky_banck.ui.theme.NavyBlue
import com.example.proyect.nucky_banck.ui.theme.TextDark
import com.example.proyect.nucky_banck.ui.theme.TextGray
import com.example.proyect.nucky_banck.ui.theme.White

@Composable
fun TransferView(
    cedula: String,
    navController: NavController,
    viewModel: TransferViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Control para mostrar el diálogo de mensaje
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    // Saber si el diálogo que se muestra es de éxito (para regresar al cerrar)
    var esExito by remember { mutableStateOf(false) }

    // Muestra el loading mientras se procesa la transferencia
    if (uiState.isLoading) {
        ShowLoadingAlertDialog()
    }

    // Muestra el diálogo con el resultado
    if (showDialog) {
        ShowMessageAlertDialog(
            onConfirmation = {
                showDialog = false
                // Si fue exitosa, regresa a Home
                if (esExito) {
                    navController.popBackStack()
                }
            },
            dialogTitle = dialogTitle,
            dialogText  = dialogMessage
        )
    }

    // Cuando la transferencia es exitosa
    LaunchedEffect(uiState.transferSuccess) {
        if (uiState.transferSuccess) {
            dialogTitle   = "Transferencia exitosa"
            dialogMessage = uiState.successMessage
            esExito       = true
            showDialog    = true
        }
    }

    // Cuando hay un error general
    LaunchedEffect(uiState.generalError) {
        uiState.generalError?.let {
            dialogTitle   = "Error en la transferencia"
            dialogMessage = it
            esExito       = false
            showDialog    = true
        }
    }

    // Fondo degradado igual al resto de la app
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(NavyBlue, DeepBlue)
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ENCABEZADO con botón para volver
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, start = 8.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector   = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text       = "Transferencia",
                        color      = White,
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text     = "Envía dinero a otro usuario",
                        color    = White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            // CARD con el formulario de transferencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 36.dp)
                ) {

                    Text(
                        text       = "Datos de la transferencia",
                        color      = TextDark,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo: cédula del destinatario
                    NuckyTextField(
                        value         = uiState.cedulaDestino,
                        onValueChange = viewModel::onCedulaDestinoChange,
                        label         = "Cédula del destinatario",
                        keyboardType  = KeyboardType.Number,
                        leadingIcon   = {
                            Icon(
                                Icons.Default.Badge,
                                contentDescription = null,
                                tint = NavyBlue
                            )
                        },
                        errorMessage  = uiState.cedulaDestinoError
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo: monto a transferir
                    NuckyTextField(
                        value         = uiState.monto,
                        onValueChange = viewModel::onMontoChange,
                        label         = "Monto a transferir",
                        keyboardType  = KeyboardType.Decimal,
                        leadingIcon   = {
                            Icon(
                                Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = NavyBlue
                            )
                        },
                        errorMessage  = uiState.montoError
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón Transferir
                    Button(
                        onClick = {
                            viewModel.onTransferirClicked(cedula)
                        },
                        enabled  = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape  = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor         = Emerald,
                            disabledContainerColor = Emerald.copy(alpha = 0.5f)
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color       = White,
                                modifier    = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text       = "Transferir",
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón cancelar / volver
                    TextButton(
                        onClick  = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text  = "Cancelar",
                            color = TextGray
                        )
                    }
                }
            }
        }
    }
}
