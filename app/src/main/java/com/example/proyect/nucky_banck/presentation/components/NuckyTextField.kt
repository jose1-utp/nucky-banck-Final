package com.example.proyect.nucky_banck.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyect.nucky_banck.ui.theme.BorderGray
import com.example.proyect.nucky_banck.ui.theme.ErrorRed
import com.example.proyect.nucky_banck.ui.theme.NavyBlue
import com.example.proyect.nucky_banck.ui.theme.TextDark
import com.example.proyect.nucky_banck.ui.theme.TextGray

@Composable
fun NuckyTextField(

    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(

        value = value,

        onValueChange = onValueChange,

        label = {
            Text(text = label)
        },

        leadingIcon = leadingIcon,

        trailingIcon = {

            if (isPassword) {

                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {

                    Icon(
                        imageVector =
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,

                        contentDescription = null
                    )
                }
            }
        },

        visualTransformation =

            if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,

        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

        singleLine = true,

        isError = errorMessage != null,

        supportingText = {

            if (errorMessage != null) {

                Text(
                    text = errorMessage,
                    color = ErrorRed,
                    fontSize = 12.sp
                )
            }
        },

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(12.dp),

        colors = OutlinedTextFieldDefaults.colors(

            focusedBorderColor = NavyBlue,

            unfocusedBorderColor = BorderGray,

            focusedLabelColor = NavyBlue,

            unfocusedLabelColor = TextGray,

            cursorColor = NavyBlue,

            focusedTextColor = TextDark,

            unfocusedTextColor = TextDark,

            focusedLeadingIconColor = NavyBlue,

            unfocusedLeadingIconColor = NavyBlue,

            focusedTrailingIconColor = NavyBlue,

            unfocusedTrailingIconColor = NavyBlue,

            errorBorderColor = ErrorRed
        )
    )
}