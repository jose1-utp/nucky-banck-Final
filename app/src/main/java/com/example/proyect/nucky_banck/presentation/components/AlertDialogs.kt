package com.example.proyect.nucky_banck.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.proyect.nucky_banck.R

@Composable
fun ShowLoadingAlertDialog() {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(id = R.string.text_loading)) },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        confirmButton = { }
    )
}

@Composable
fun ShowMessageAlertDialog(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        onDismissRequest = { onConfirmation() },
        title = { Text(text = dialogTitle) }, // Corregido: Sin stringResource
        text = { Text(text = dialogText) },   // Corregido: Sin stringResource
        confirmButton = {
            Button(onClick = { onConfirmation() }) {
                Text(stringResource(id = R.string.btn_accept))
            }
        }
    )
}
