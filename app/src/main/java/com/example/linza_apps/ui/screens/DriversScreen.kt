package com.example.linza_apps.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.copy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.trace
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.AppBar
import com.example.linza_apps.ui.components.DateTimeDisplay
import com.example.linza_apps.ui.components.DriverViewModel
import com.example.linza_apps.ui.components.Tabs
import com.example.linza_apps.ui.screens.NewOrderContent
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun DriverScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, -1)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            DriverContent(navController, Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun DriverContent(
    navController: NavController,
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
            Drivers()
        }
    }
}

@Composable
fun Drivers(
    modifier: Modifier = Modifier,
    //showDialog: Boolean,
    //onDismissRequest: () -> Unit
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var checkId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var updateName by remember { mutableStateOf("") }
    var updatePhone by remember { mutableStateOf("") }

    val driverVm: DriverViewModel = viewModel()
    val drivers by driverVm.fetchDrivers().collectAsState(emptyList())

    var editDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    val db = Firebase.firestore
    val context = LocalContext.current

    // Put into a Driver ViewModel in Components.kt
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add a Driver") },
            text = {
                Column() {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val driverRef = db.collection("Drivers").document()

                    val driver = hashMapOf(
                        "id" to driverRef.id,
                        "name" to name.first().uppercaseChar() + name.substring(1).lowercase(),
                        "phone" to phoneNumber.toLong()
                    )
                    id = ""
                    name = ""
                    phoneNumber = ""

                    driverRef.set(driver)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context, "Driver Added Successfully", Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context, "Error Adding Driver", Toast.LENGTH_SHORT
                            ).show()
                        }
                    showDialog = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (editDialog) {
        AlertDialog(
            onDismissRequest = { editDialog = false },
            title = { Text("Edit Driver Details") },
            text = {
                Log.e("EditDriver", "1. id: ${checkId}\nname: ${updateName}\nphone: ${updatePhone}")
                Column {
                    OutlinedTextField(
                        value = updateName,
                        onValueChange = { updateName = it },
                        label = { Text("Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = updatePhone,
                        onValueChange = { updatePhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    try {
                        val driverRef = db.collection("Drivers").document(checkId)
                        Log.e(
                            "EditDriver",
                            "2. id: $checkId\nname: $updateName\nphone: $updatePhone"
                        )


                        driverRef.update(
                            mapOf(
                                "name" to updateName.first().uppercaseChar() + updateName.substring(
                                    1
                                ).lowercase(),
                                "phone" to updatePhone.toLong()
                            )
                        )
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context, "Driver Added Successfully", Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context, "Error Adding Driver", Toast.LENGTH_SHORT
                                ).show()
                            }
                    } catch (e: Exception) {
                        Log.e("EditDriver", e.toString())
                    }

                    checkId = ""
                    updateName = ""
                    updatePhone = ""

                    editDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                Row {
                    Button(
                        onClick = {
                            showPopup = true
                            editDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.4f),
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) { Text("Delete") }
                    TextButton(onClick = {
                        checkId = ""
                        updateName = ""
                        updatePhone = ""
                        editDialog = false
                    }) { Text("Cancel") }
                }
            }
        )
    }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = {
                checkId = ""
                updateName = ""
                updatePhone = ""
                showPopup = false
            },
            title = { Text("Do you want to delete $updateName") },
            text = {},
            confirmButton = {
                Button(onClick = {
                    val driverRef = db.collection("Drivers").document(checkId)

                    driverRef.delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                context, "Driver Deleted", Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context, "Error Deleting Driver", Toast.LENGTH_SHORT
                            ).show()
                        }
                    checkId = ""
                    updateName = ""
                    updatePhone = ""
                    showPopup = false
                }) { Text("Yes") }
            },
            dismissButton = { TextButton(onClick = { showPopup = false }) { Text("No") } }
        )
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Column {
            //Test UI
            Box(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Text("Manage Delivery Drivers", Modifier.align(Alignment.Center), Color.Black)
            }
            HorizontalDivider()
            Button(onClick = {
                showDialog = true
            }, Modifier.align(Alignment.End)) {
                Text("Add Driver")
            }
            HorizontalDivider()
            LazyColumn {
                Log.e("Drivers", drivers.toString())
                items(drivers) { driver ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                    ) {
                        //Pass Driver Id and pass it to button on click for edit showPopup
                        Text(
                            "${driver.name} - ${driver.phone}",
                            Modifier.align(Alignment.CenterStart),
                            Color.Black
                        )
                        Button(onClick = {
                            updateName = driver.name
                            updatePhone = driver.phone.toString()
                            checkId = driver.id
                            editDialog = true
                        }, Modifier.align(Alignment.CenterEnd)) { Text("Edit") }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}