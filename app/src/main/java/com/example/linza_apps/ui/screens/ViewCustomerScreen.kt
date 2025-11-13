package com.example.linza_apps.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.navigation.Screen
import com.example.linza_apps.ui.components.AppBar
import com.example.linza_apps.ui.components.CustomTextField
import com.example.linza_apps.ui.components.CustomerViewModel
import com.example.linza_apps.ui.components.DateTimeDisplay
import com.example.linza_apps.ui.components.LocationSearchField
import com.example.linza_apps.ui.components.OrderItemsList
import com.example.linza_apps.ui.components.Tabs
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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
            ViewCustomerBox(customerId, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCustomerBox(
    customerId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: CustomerViewModel = viewModel()
    val cus by viewModel.getCustomerId(customerId).collectAsState(null)
    val orders by viewModel.getCustomerOrders(customerId).collectAsState(emptyList())
    val customer = cus
    var selectedOrder by remember { mutableStateOf<OrderItemsList?>(null) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var updateName by remember { mutableStateOf(customer?.name ?: "") }
    var updateAddress by remember { mutableStateOf(customer?.address ?: "") }
    var updatePhone by remember { mutableStateOf(customer?.phone ?: "") }
    var latLng by remember { mutableStateOf(LatLng(6.9691122, 79.9206079)) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                updateName = ""
                updateAddress = ""
                updatePhone = ""
            },
            title = { Text("Edit Customer Details") },
            text = {
                Column {
                    OutlinedTextField(
                        value = updateName,
                        onValueChange = { updateName = it },
                        label = { Text("Name") }
                    )
                    OutlinedTextField(
                        value = updatePhone,
                        onValueChange = { updatePhone = it },
                        label = { Text("Phone Number") }
                    )
                    LocationSearchField(
                        address = updateAddress,
                        onQueryChange = { updateAddress = it },
                        onPlaceSelected = { selected ->
                            if (selected != null) {
                                latLng = selected
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (customer != null) {
                        val customerRef = Firebase.firestore
                            .collection("Customers")
                            .document(customerId)
                        customerRef.update(
                            mapOf(
                                "name" to updateName.trim().lowercase(),
                                "phone" to updatePhone,
                                "address" to updateAddress,
                                "lat" to latLng.latitude,
                                "long" to latLng.longitude
                            )
                        )
                    }
                    showDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                Row {
                    Button(
                        onClick = {
                            showDialog = false
                            showPopup = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.4f),
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) { Text("Delete") }
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                }

            }
        )
    }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text("Are you sure you want to delete this customer?") },
            text = { Text(customer!!.name) },
            confirmButton = {
                Button(onClick = {
                    val customerRef =
                        Firebase.firestore.collection("Customers").document(customerId)

                    customerRef.delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                context, "Customer ${customer!!.name} Deleted", Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Screen.Customers.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Error deleting customer ${customer!!.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    showPopup = false
                })
                { Text("Yes") }
            },
            dismissButton = {
                TextButton(
                    onClick =
                        { showPopup = false })
                { Text("No") }
            }
        )
    }

    //if (customer != null){
    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .weight(1f)
                .background(Color.White)
                .fillMaxSize()
        ) {
            //if (!showDialog) {
            customer?.let {
                Column() {
                    Text(
                        color = Color.Black,
                        text = "Customer: ${customer.name}\nPhone Number: ${customer.phone}\nAddress: ${customer.address}\n"
                    )
                    Button(onClick = {
                        //Add customer details editing logic
                        //Use something like alert dialog which has a form to edit customer details
                        updateName = customer?.name ?: ""
                        updateAddress = customer?.address ?: ""
                        updatePhone = customer?.phone ?: ""
                        showDialog = true
                    }) {
                        Text("Edit Customer Details")
                    }
                }
            }
            //}
        }
        Box(
            Modifier
                .weight(3f)
                .fillMaxSize()
        ) {
            Row {
                Box(
                    Modifier
                        .weight(1f)
                        .background(Color.Gray)
                        .fillMaxSize()
                ) {
                    Column {
                        Box(
                            Modifier
                                .weight(3f)
                                .fillMaxSize()
                        ) {
                            selectedOrder?.let { order ->
                                Column {
                                    Text(
                                        color = Color.Black,
                                        text = "Ordered on: ${order.date}"
                                    )
                                    LazyColumn {
                                        itemsIndexed(order.items) { index, item ->
                                            Text(
                                                color = Color.Black,
                                                text = "${index + 1}. ${item.name} (${item.size}) x${item.quantity} - Rs.${item.price * item.quantity}"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .background(Color.White)
                                .fillMaxSize()
                        ) {
                            selectedOrder?.let { order ->
                                Text(
                                    color = Color.Black,
                                    text = "Total Price: Rs.${order.total}"
                                )
                            }
                        }
                    }
                }
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
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
                        Box(
                            Modifier
                                .weight(5f)
                                .background(Color.LightGray)
                                .fillMaxSize()
                        ) {
                            LazyColumn {
                                items(orders) { order ->
                                    Text(
                                        modifier = Modifier.clickable {
                                            selectedOrder = order
                                        },
                                        text = "Order Date: ${order.date}  --  Delivery: ${order.delivery}",
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
