package com.example.linza_apps.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.*
import com.example.linza_apps.ui.theme.backgroundDark
import com.example.linza_apps.ui.screens.NewOrderContent
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.forEach

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
    val initialLatLng = LatLng(6.9690506, 79.9207371)
    val selectedDeliveries = remember { mutableStateListOf<DeliveryOrder>() }

    Box(Modifier.fillMaxSize()) {
        Column() {
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(6f)
            ) {
                //Order ID Column
                Box(
                    Modifier
                        .background(Color.White)
                        .fillMaxHeight()
                        .weight(2f)
                ) {
                    Column {
                        Box(
                            Modifier
                                .background(Color.Gray)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Order ID",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black
                            )
                        }
                        Box() {
                            Column() { //Column for test text
                                LazyColumn {
                                    itemsIndexed(deliveries) { index, delivery ->
                                        if (!delivery.status) {
                                            Text(
                                                "${(index + 1)} - ${delivery.orderId}",
                                                color = Color.Black
                                            )
                                            HorizontalDivider()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //Address Column
                Box(
                    Modifier
                        .background(Color.Gray)
                        .fillMaxHeight()
                        .weight(8f)
                ) {
                    Column {
                        Box(
                            Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Address",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black
                            )
                        }
                        Box() {

                            Column() { //Column for test text
                                LazyColumn {
                                    Log.e("DelDri", "Deliveries: ${deliveries.toString()}")
                                    Log.e("DelDri", "Drivers: ${drivers.toString()}")
                                    items(deliveries) { delivery ->
                                        if (!delivery.status) {
                                            val isSelected = selectedDeliveries.contains(delivery)
                                            Text(
                                                text = delivery.address,
                                                color = Color.Black,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (isSelected) Color(0xFFD0F0C0) else Color.Transparent)
                                                    .clickable {
                                                        if (isSelected) {
                                                            selectedDeliveries.remove(delivery)
                                                        } else {
                                                            selectedDeliveries.add(delivery)
                                                        }
                                                    })
                                            HorizontalDivider()
                                        }
                                        /*
                                        make it clickable
                                        when clicked, add delivery order to driver - assign order to temp delivery order item
                                        and delete order from deliveries collection.
                                        have some kinda flag to know a delivery was clicked
                                        */

                                    }
                                }
                            }
                        }
                    }
                }
                var driverDetails by remember { mutableStateOf(false) }
                var selectedDriver by remember { mutableStateOf<Driver?>(null) }

                if (driverDetails) {
                    val deliveryRef = Firebase.firestore.collection("Deliveries")
                    val driverRef = Firebase.firestore.collection("Drivers")
                    AlertDialog(
                        onDismissRequest = { driverDetails = false },
                        title = {
                            if (selectedDriver != null) {
                                Text(selectedDriver!!.name)
                            }
                        },
                        text = {
                            Column {
                                val driverDeliveries =
                                    driverVm.getDriverDeliveries(selectedDriver!!.id)
                                        .collectAsState(emptyList()).value
                                Text("Return from Delivery?")
                                LazyColumn {
                                    items(driverDeliveries) { delivery ->
                                        Text(delivery.address)
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            selectedDriver?.let {
                                val driverDeliveries =
                                    driverVm.getDriverDeliveries(selectedDriver!!.id)
                                        .collectAsState(emptyList()).value
                                Row {
                                    Button(onClick = {
                                        driverDeliveries.forEach { delivery ->
                                            deliveryRef.document(delivery.deliveryId).delete()
                                        }
                                        driverRef.document(selectedDriver!!.id)
                                            .update(
                                                mapOf(
                                                    "deliveries" to null,
                                                    "status" to false
                                                )
                                            )
                                        driverDetails = false
                                    }) { Text("Return") }
                                    TextButton(onClick = {
                                        driverDetails = false
                                    }) { Text("Cancel") }
                                }
                            }
                        },
                        dismissButton = {
                            selectedDriver?.let {
                                val driverDeliveries =
                                    driverVm.getDriverDeliveries(selectedDriver!!.id)
                                        .collectAsState(emptyList()).value
                                Button(onClick = {
                                    driverDeliveries.forEach { delivery ->
                                        deliveryRef.document(delivery.deliveryId)
                                            .update("status", false)
                                    }
                                    driverRef.document(selectedDriver!!.id)
                                        .update(
                                            mapOf(
                                                "deliveries" to null,
                                                "status" to false
                                            )
                                        )
                                    driverDetails = false
                                }) { Text("Unassign Deliveries") }
                            }
                        }
                    )
                }

                //Out for Delivery Column
                Box(
                    Modifier
                        .background(Color.White)
                        .fillMaxHeight()
                        .weight(3f)
                ) {
                    Column {
                        Box(
                            Modifier
                                .background(Color.Gray)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Out for Delivery",
                                //fontSize = 30.sp,
                                modifier = Modifier.align(Alignment.Center), color = Color.Black
                            )
                        }
                        Box() {
                            LazyColumn {
                                items(drivers) { driver ->
                                    //  An if statement to display assigned and unassigned (status)
                                    if (driver.status) {
                                        Text(
                                            text = driver.name,
                                            color = Color.Black,
                                            //fontSize = 20.sp
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedDriver = driver
                                                    driverDetails = true
                                                }
                                        ) //Make it clickable and assigned deliveries inside it
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }

                //Available Drivers Column
                Box(
                    Modifier
                        .background(Color.Gray)
                        .fillMaxHeight()
                        .weight(3f)
                ) {
                    Column {
                        Box(
                            Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "Available Drivers",
                                modifier = Modifier.align(Alignment.Center), color = Color.Black
                            )
                        }
                        Box() {
                            LazyColumn {
                                items(drivers) { driver ->

                                    if (!driver.status) {
                                        Text(
                                            text = driver.name,
                                            color = Color.Black,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    assignDeliveries(selectedDeliveries, driver)
                                                    selectedDeliveries.clear()
                                                })
                                        /*
                                            make clickable and using flag for delivery click
                                            assign temp order to driver list of delivery orders
                                             */
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            var showMap by remember { mutableStateOf(false) }

            if (showMap) {
                Column {
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(9f),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(initialLatLng, 15f)
                        }
                    ) {
                        Marker(
                            state = MarkerState(initialLatLng),
                            title = "Linza",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)

                        )
                        deliveries.forEachIndexed { i, del ->
                            Marker(
                                state = MarkerState(LatLng(del.lat, del.long)),
                                title = (i + 1).toString(),
                            )
                        }
                    }
                    Button(
                        onClick = { showMap = false },
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterHorizontally)
                    ) { Text("Back") }
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .weight(1f)
            ) {
                Button(
                    onClick = {
                        showMap = true
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) { Text("Map") }

            }
        }
    }
}

fun assignDeliveries(selectedDeliveries: SnapshotStateList<DeliveryOrder>, driver: Driver) {
    val deliveryRef = Firebase.firestore.collection("Deliveries")
    val driverRef = Firebase.firestore.collection("Drivers")
    if (selectedDeliveries.isNotEmpty()) {
        driverRef.document(driver.id)
            .update(
                mapOf(
                    "deliveries" to selectedDeliveries,
                    "status" to true
                )
            )
            .addOnSuccessListener {
                Log.d("FireStore", "Driver updated")
            }
            .addOnFailureListener {
                Log.e("FireStore", "Error updating Driver")
            }
        selectedDeliveries.forEach { del ->
            deliveryRef.document(del.deliveryId)
                .update("status", true)
                .addOnSuccessListener {
                    Log.d(
                        "FireStore",
                        "Delivery Status True"
                    )
                }
                .addOnFailureListener {
                    Log.e(
                        "FireStore",
                        "Error updating delivery"
                    )
                }
        }
    }
}
