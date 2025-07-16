package com.example.linza_apps.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.linza_apps.ui.components.AppBar
import com.example.linza_apps.ui.components.Tabs

@Composable
fun ViewCustomerScreen(navController: NavController, customerId: String, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, 3)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            DashboardContent(navController, Modifier.padding(innerPadding))
        }
    )
}

