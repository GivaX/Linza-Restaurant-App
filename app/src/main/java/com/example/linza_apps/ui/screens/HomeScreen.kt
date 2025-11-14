package com.example.linza_apps.ui.screens

import android.util.Log
import android.widget.Toast
import com.example.linza_apps.navigation.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.linza_apps.MyApp
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
            PrinterControls()
            ManageDriversButton(navController)

        }
    }

}

@Composable
fun PrinterControls(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val app = MyApp.instance
    var showPrinterSelecter by remember { mutableStateOf(false) }
    var printerStatus by remember { mutableStateOf("No Printer Selected") }
    var isPrinterReady by remember { mutableStateOf(false) }

    LaunchedEffect(app.usbHelper.isPrinterSelected()) {
        Log.d("Printer", "Printer Status LE Loop Check")
        if (app.usbHelper.isPrinterSelected()) {
            val printer = app.usbHelper.getCachedPrinter()
            printerStatus = if (printer != null && app.usbHelper.hasPermission()) {
                isPrinterReady = true
                printer.deviceName
            } else if (printer != null) {
                isPrinterReady = false
                "${printer.deviceName} (no permission)"
            } else {
                isPrinterReady = false
                "No Printer Selected"
            }
        }
    }

    if (showPrinterSelecter) {
        PrinterSelecterDialog(
            onDismiss = { showPrinterSelecter = false },
            onPrinterSelected = { device ->
                app.usbHelper.selectPrinter(device)
                printerStatus = "${device.deviceName} (grant permission)"
                showPrinterSelecter = false
            }
        )
    }

    Card(
        modifier = Modifier
            .width(500.dp)
            .height(250.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Printer Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (isPrinterReady) Icons.Default.CheckCircle else Icons.Filled.Build,
                    contentDescription = null,
                    tint = if (isPrinterReady) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = printerStatus,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Select Printer Button
                Button(
                    onClick = { showPrinterSelecter = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Select Printer")
                }

                // Test Print Button
                Button(
                    onClick = {
                        val success = app.usbHelper.testConnection()
                        Toast.makeText(
                            context,
                            if (success) "Print successful!" else "Print failed - check printer",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    enabled = isPrinterReady,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Test Print")
                }
            }

            if (app.usbHelper.isPrinterSelected()) {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        app.usbHelper.clearSelectedPrinter()
                        printerStatus = "No printer selected"
                        isPrinterReady = false
                        Toast.makeText(context, "Printer Cleared", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Clear Selection")
                }
            }
        }
    }
}

@Composable
fun PrinterSelecterDialog(
    onDismiss: () -> Unit,
    onPrinterSelected: (android.hardware.usb.UsbDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val app = MyApp.instance
    val devices = remember { app.usbHelper.getAllUsbDevices() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select USB Printer") },
        text = {
            if (devices.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No USB devices detected",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Please connect a USB printer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    items(devices) { device ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onPrinterSelected(device)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = device.deviceName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "VID: 0x${device.vendorId.toString(16).uppercase()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "PID: 0x${device.productId.toString(16).uppercase()}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                // Show interface info
                                if (device.interfaceCount > 0) {
                                    val iface = device.getInterface(0)
                                    Text(
                                        text = "Class: ${iface.interfaceClass}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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