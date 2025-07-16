package com.example.linza_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.AppBar
import com.example.linza_apps.ui.components.CustomerViewModel
import com.example.linza_apps.ui.components.DateTimeDisplay
import com.example.linza_apps.ui.components.Tabs
import com.google.firebase.Firebase

@Composable
fun ViewCustomerScreen(
    navController: NavController,
    customerId: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, 3)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            ViewCustomersContent(navController, customerId, Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun ViewCustomersContent(
    navController: NavController,
    customerId: String,
    modifier: Modifier = Modifier
) {
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
            ViewCustomerBox(customerId)
        }
    }
}

@Composable
fun ViewCustomerBox(customerId: String, modifier: Modifier = Modifier) {
    val viewModel: CustomerViewModel = viewModel()
    val cus by viewModel.getCustomerId(customerId).collectAsState(null)
    val orders by viewModel.getCustomerOrders(customerId).collectAsState(emptyList())
    val customer = cus
    //if (customer != null){
    Column(Modifier.fillMaxSize()) {
        Box(Modifier
            .weight(1f)
            .background(Color.White)
            .fillMaxSize()) {
            customer?.let {
                Column() {
                    Text(
                        color = Color.Black,
                        text = "Customer: ${customer.name}\nPhone Number: ${customer.phone}\nAddress: ${customer.address}\n"
                    )
                    Button(onClick = {
                        //Add customer details editing logic
                        //Use something like alert dialog which has a form to edit customer details
                    }) {
                        Text("Edit Customer Details")
                    }
                }
            }
        }
        Box(Modifier
            .weight(3f)
            .fillMaxSize()) {
            Row {
                Box(Modifier
                    .weight(1f)
                    .background(Color.Gray)
                    .fillMaxSize()) {
                    Column {
                        Box(Modifier
                            .weight(3f)
                            .fillMaxSize()) { }
                        Box(Modifier
                            .weight(1f)
                            .background(Color.White)
                            .fillMaxSize()) { }
                    }
                }
                Box(Modifier
                    .weight(1f)
                    .fillMaxSize()) {
                    Column {
                        Box(
                            Modifier
                                .weight(1f)
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxSize()
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.secondary,
                                text = "Order History"
                            )
                        }
                        Box(Modifier
                            .weight(5f)
                            .background(Color.LightGray)
                            .fillMaxSize()) {
                            LazyColumn {
                                items(orders) { order ->
                                    Text(
                                        text = "Order Date: ${order.date}",
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //}
}
