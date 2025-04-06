package com.example.linza_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.*
import com.example.linza_apps.ui.theme.backgroundDark
import com.example.linza_apps.ui.screens.NewOrderContent

@Composable
fun DeliveryScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, 2)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            DeliveryContent(Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun DeliveryContent(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.linza_background), // Replace with your PNG
            contentDescription = "Background",
            contentScale = ContentScale.Fit, // Choose how the image fills the space
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF0D111C))
        )
        Column {
            DateTimeDisplay()
        }
    }

}

