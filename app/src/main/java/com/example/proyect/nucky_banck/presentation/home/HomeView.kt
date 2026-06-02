package com.example.proyect.nucky_banck.presentation.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyect.nucky_banck.domain.model.Movimiento
import com.example.proyect.nucky_banck.ui.theme.BorderGray
import com.example.proyect.nucky_banck.ui.theme.DeepBlue
import com.example.proyect.nucky_banck.ui.theme.Emerald
import com.example.proyect.nucky_banck.ui.theme.NavyBlue
import com.example.proyect.nucky_banck.ui.theme.TextDark
import com.example.proyect.nucky_banck.ui.theme.TextGray
import com.example.proyect.nucky_banck.ui.theme.White
import com.example.proyect.nucky_banck.R
import java.io.File

@Composable
fun HomeView(
    cedula: String,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val movimientos by viewModel.movimientos.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val photoFile = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
    val photoUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
    val localPhoto by viewModel.localPhoto.collectAsStateWithLifecycle()
    val camaraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            success ->
        if(success){
            viewModel.uploadPhoto(cedula, photoUri)
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            camaraLauncher.launch(photoUri)
        }
    }
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
                nombre = uiState.fullName,
                onLogout = { showLogoutDialog = true },
                photoUrl = uiState.photoUrl,
                onPhotoClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                localphoto = localPhoto
            )

            HomeCard(
                saldo = uiState.saldo,
                cedula = cedula,
                movimientos = movimientos,
                navController = navController
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                    ) {
                        Text(stringResource(R.string.btn_exit), color = White)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text(stringResource(R.string.btn_cancel), color = TextGray)
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.text_question_out),
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.text_question_out_cond),
                        color = TextDark.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = White
            )
        }
    }
}

@Composable
private fun TopBar(nombre: String, onLogout: () -> Unit, photoUrl: String, onPhotoClick: () -> Unit, localphoto: Uri?) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 56.dp, start = 24.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = stringResource(R.string.text_welcome_again),
                color = White.copy(alpha = 0.75f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            AsyncImage(
                model = if (localphoto != null) {
                    localphoto
                } else if (photoUrl.isNotEmpty()) {
                    photoUrl
                } else {
                    android.R.color.white
                },
                contentDescription = "Foto perfil",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable{ onPhotoClick() }
                    .border(
                        width = 3.dp,
                        color = White,
                        shape = CircleShape
                    )

            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = nombre,
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(onClick = onLogout) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = White
            )
        }
    }
}

@Composable
private fun HomeCard(saldo: Double, cedula: String, movimientos: List<Movimiento>, navController: NavController) {

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

            Text(text = stringResource(R.string.text_saldo), color = TextGray, fontSize = 14.sp)

            Text(
                text = "$%,.2f".format(saldo),
                color = NavyBlue,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Emerald.copy(alpha = 0.12f)
            ) {
                Text(
                    text = stringResource(R.string.title_account),
                    color = Emerald,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(color = BorderGray)
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.title_actions),
                color = TextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                ActionButton(
                    label = stringResource(R.string.btn_transfer),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("transfer/$cedula") }
                )

                ActionButton(
                    label = stringResource(R.string.btn_history),
                    modifier = Modifier.weight(1f),
                    onClick  = { navController.navigate("historial/$cedula") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

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
        onClick = onClick,
        modifier = modifier.height(68.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyBlue),
        border = ButtonDefaults.outlinedButtonBorder.copy()
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
