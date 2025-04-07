package com.example.linza_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.AppBar
import com.example.linza_apps.ui.components.DateTimeDisplay
import com.example.linza_apps.ui.components.EnterDetails
import com.example.linza_apps.ui.components.Tabs
import com.example.linza_apps.ui.screens.NewOrderContent

@Composable
fun AddCustomerScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold (
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, 3)

            }
        },
        content = { innerPadding ->
            AddCustomersContent(Modifier.padding(innerPadding))
        }
    )
}

@Preview
@Composable
fun AddCustomersContent(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.linza_background), // Replace with your PNG
            contentDescription = "Background",
            contentScale = ContentScale.Fit, // Choose how the image fills the space
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF0D111C))
                .graphicsLayer(alpha = 0.2f)
        )
        Column {
            DateTimeDisplay()
            EnterDetails()
        }
    }
}
