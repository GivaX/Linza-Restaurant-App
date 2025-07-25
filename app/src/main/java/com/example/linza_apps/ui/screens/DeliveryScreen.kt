package com.example.linza_apps.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
            Deliveries()
        }
    }

}

@Preview
@Composable
fun Deliveries() {
    val deliveryVm: DeliveryViewModel = viewModel()
    val driverVm: DriverViewModel = viewModel()
    val deliveries by deliveryVm.fetchDeliveries().collectAsState(emptyList())
    val drivers by driverVm.fetchDrivers().collectAsState(emptyList())
    Box(Modifier.fillMaxSize()) {
        Column() {
            Row(Modifier
                .fillMaxWidth()
                .weight(6f)) {
                //Order ID Column
                Box(Modifier
                    .background(Color.White)
                    .fillMaxHeight()
                    .weight(2f)) {
                    Column {
                        Box(Modifier
                            .background(Color.Gray)
                            .fillMaxWidth()) {
                            Text("Order ID",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }
                        Box() {
                            Column() { //Column for test text
                            LazyColumn {
                                items(deliveries) { delivery ->
                                    Text(delivery.orderId, color = Color.Black)
                                }
                            }
                            HorizontalDivider()

                                Text("Test Filler Text", color = Color.Black)
                                Text("1.", color = Color.Black)
                                Text("2.", color = Color.Black)
                                Text("3.", color = Color.Black)
                            }
                        }
                    }
                }

                //Address Column
                Box(Modifier
                    .background(Color.Gray)
                    .fillMaxHeight()
                    .weight(8f)) {
                    Column {
                        Box(Modifier
                            .background(Color.White)
                            .fillMaxWidth()) {
                            Text("Address",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }
                        Box() {
                            Column() { //Column for test text
                                LazyColumn {
                                    Log.e("DelDri", "Deliveries: ${deliveries.toString()}" )
                                    Log.e("DelDri", "Drivers: ${drivers.toString()}")
                                    items(deliveries) { delivery ->
                                        Text(delivery.address, color = Color.Black)
                                    }
                                }
                                HorizontalDivider()

                                Text("Test Filler Text", color = Color.Black)
                                Text("144/4 Wewalduwa Rd, Kelaniya", color = Color.Black)
                                Text("Wattala", color = Color.Black)
                                Text("Dalugama", color = Color.Black)
                            }
                        }
                    }
                }

                //Out for Delivery Column
                Box(Modifier
                    .background(Color.White)
                    .fillMaxHeight()
                    .weight(3f)) {
                    Column {
                        Box(Modifier
                            .background(Color.Gray)
                            .fillMaxWidth()) {
                            Text(text = "Out for Delivery",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }
                        Box() {
                            LazyColumn {
                                items(drivers) { driver ->
                                    /*  An if statement to display assigned and unassigned (status)
                                    if (driver.status == "assigned") {
                                        Text(driver.name, color = Color.Black) //Make it clickable and assigned deliveries inside it
                                    }*/
                                }
                            }
                            Column() { //Column for test text
                                Text("Test Filler Text", color = Color.Black)
                                Text("Driver 1", color = Color.Black)
                                Text("Driver 4", color = Color.Black)
                            }
                        }
                    }
                }

                //Available Drivers Column
                Box(Modifier
                    .background(Color.Gray)
                    .fillMaxHeight()
                    .weight(3f)) {
                    Column {
                        Box(Modifier
                            .background(Color.White)
                            .fillMaxWidth()) {
                            Text("Available Drivers",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black)
                        }
                        Box() {
                            LazyColumn {
                                items(drivers) { driver ->
                                    Text(driver.name, color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }
            Box(Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .weight(1f)) {
                Text("Map buttons etc",
                    modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

