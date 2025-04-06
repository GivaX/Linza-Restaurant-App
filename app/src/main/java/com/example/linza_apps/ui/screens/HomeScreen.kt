package com.example.linza_apps.ui.screens

import com.example.linza_apps.navigation.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, -1)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            DashboardContent(navController, Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun DashboardContent(navController: NavController, modifier: Modifier = Modifier) {
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
            ManageDriversButton(navController)
        }
    }

}

@Composable
fun ManageDriversButton(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Button(
            onClick = {
                //Nav to manage drivers screen
                navController.navigate(Screen.Drivers.route) {
                    popUpTo(Screen.Home.route) {
                        saveState = true
                        inclusive = true}
                    launchSingleTop = true

                }

            },
        ) {
            Text("Manage Drivers")
        }
    }

}