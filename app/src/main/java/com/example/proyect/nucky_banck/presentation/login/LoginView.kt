package com.example.proyect.nucky_banck.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
fun LoginView(
    viewModel: LoginViewModel = viewModel(),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Control para el diálogo de error
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf(0) }

    // Muestra loading mientras Firebase verifica
    if (uiState.isLoading) {
        ShowLoadingAlertDialog()
    }

    // Muestra el error si hubo alguno
    if (showDialog) {
        ShowMessageAlertDialog(
            onConfirmation = { showDialog = false },
            dialogTitle = stringResource(R.string.dialog_error_title),
            dialogText = stringResource(message)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(NavyBlue, DeepBlue))
            )
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
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.text_welcome),
                    fontSize = 42.sp,
                    color = White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    color = White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.app_phrase),
                    color = White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            // CARD con formulario de login
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp)
                ) {

                    Text(
                        text = stringResource(R.string.label_login),
                        color = TextDark,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo: cédula
                    NuckyTextField(
                        value = uiState.cedula,
                        onValueChange = viewModel::onCedulaChange,
                        label = stringResource(R.string.label_document_number),
                        keyboardType = KeyboardType.Number,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NuckyTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = stringResource(R.string.label_password),
                        isPassword = true,

                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón iniciar sesión
                    Button(
                        onClick = {
                            viewModel.onLoginClicked(
                                onSuccess = {
                                    navController.navigate("home/${uiState.cedula}") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onError = {
                                    message = it
                                    showDialog = true
                                }
                            )
                        },
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald)
                    ) {

                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.btn_login),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Link para ir a registro
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.text_no_account),
                            color = TextGray
                        )
                        TextButton(
                            onClick = { navController.navigate("register") }
                        ) {
                            Text(
                                text = stringResource(R.string.text_register),
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
