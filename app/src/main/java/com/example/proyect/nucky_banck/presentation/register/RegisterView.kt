package com.example.proyect.nucky_banck.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyect.nucky_banck.R
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
fun RegisterView(
    viewModel: RegisterViewModel = viewModel(),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Control del diálogo de resultado
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf(0) }
    // Saber si el diálogo es de éxito para regresar al login
    var esExito by remember { mutableStateOf(false) }

    // Muestra loading mientras Firebase guarda el usuario
    if (uiState.isLoading) {
        ShowLoadingAlertDialog()
    }

    // Muestra el diálogo de resultado
    if (showDialog) {
        ShowMessageAlertDialog(
            onConfirmation = {
                showDialog = false
                // Si fue exitoso, vuelve al login
                if (esExito) {
                    navController.popBackStack()
                }
            },
            dialogTitle = if (esExito) stringResource(R.string.dialog_success_title) else stringResource(R.string.dialog_error_title),
            dialogText = stringResource(message)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(NavyBlue, DeepBlue)))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ENCABEZADO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.register_title),
                    color = White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.register_title_phrase),
                    color = White.copy(alpha = 0.8f),
                    fontSize = 15.sp
                )
            }

            // CARD con formulario de registro
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 36.dp)
                ) {

                    Text(
                        text = stringResource(R.string.register_title_card),
                        color = TextDark,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo: nombre completo
                    NuckyTextField(
                        value = uiState.fullName,
                        onValueChange = viewModel::onFullNameChange,
                        label = stringResource(R.string.label_full_name),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo: cédula
                    NuckyTextField(
                        value = uiState.cedula,
                        onValueChange = viewModel::onCedulaChange,
                        label = stringResource(R.string.label_document_number),
                        keyboardType = KeyboardType.Number,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo: contraseña
                    NuckyTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = stringResource(R.string.label_password),
                        isPassword = true

                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo: confirmar contraseña
                    NuckyTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { viewModel.onConfirmPasswordChange(it) },
                        label = stringResource(R.string.label_confirm_password),
                        isPassword = true

                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón registrarse
                    Button(
                        onClick = {
                            viewModel.onRegisterClicked(
                                onSuccess = {
                                    // Registro exitoso: muestra diálogo y luego va al login
                                    message = R.string.register_success_message
                                    esExito = true
                                    showDialog = true
                                },
                                onError = {
                                    // Hubo un error
                                    message = it
                                    esExito = false
                                    showDialog = true
                                }
                            )
                        },
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Emerald,
                            disabledContainerColor = Emerald.copy(alpha = 0.5f)
                        )
                    ) {

                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.btn_register),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(R.string.text_already_have_account),
                            color = TextGray
                        )

                        TextButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text(
                                text = stringResource(R.string.text_login_here),
                                color = NavyBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
