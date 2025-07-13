package com.example.linza_apps.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.*
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


@Composable
fun MenuScreen(navController: NavController, customerId: String, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, 1)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            MenuContent(customerId, Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun MenuContent(customerId: String, modifier: Modifier = Modifier) {
    val viewModel: CustomerViewModel = viewModel()
    val menuVM: MenuViewModel = viewModel()
    val customer by viewModel.getCustomerId(customerId).collectAsState(initial = null)
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
            val currentCustomer = customer
            if (customer != null)
                customer?.let {
                    Menu(viewModel = menuVM, customer = currentCustomer)
                }
            else {
                //Text("Testing")
                Menu(viewModel = menuVM, customer = null)
            }

            /*customer?.let {
                Text(customerId)
            }
            Text("Testing")*/

        }
    }
}

data class LookupRequest(
    val menuNumber: Int,
    val size: String
)

@Composable
fun Menu(modifier: Modifier = Modifier, viewModel: MenuViewModel, customer: Customer?) {
    var input by remember { mutableStateOf("") }
    //var price by remember { mutableStateOf("") }
    //var itemName by remember { mutableStateOf("") }
    //var pendingSize by remember { mutableStateOf<String?>(null) }
    var lookupRequest by remember { mutableStateOf<LookupRequest?>(null) }


    val selectedItems = remember { mutableStateListOf<OrderItem>() }
    val menuItem by viewModel.menuItem.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(lookupRequest) {
        lookupRequest?.let { request ->
            viewModel.fetchItem(request.menuNumber)
        }
    }

    LaunchedEffect(menuItem, lookupRequest) {
        val request = lookupRequest
        val item = menuItem

        if (item != null && request != null && item.menuNumber == request.menuNumber) {
            val selectedPrice = when (request.size) {
                /*"S" -> item.priceSmall?.let { "Rs.$it" } ?: run {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                    "null"
                }
                "M" -> item.priceMedium?.let { "Rs.$it" } ?: run {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                    "null"
                }
                "L" -> item.priceLarge?.let { "Rs.$it" } ?: run {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                    "null"
                }*/
                "S" -> item.priceSmall
                "M" -> item.priceMedium
                "L" -> item.priceLarge
                else -> null
            }
            if (selectedPrice != null) {
                val newItem = OrderItem(
                    menuNumber = item.menuNumber,
                    name = item.name,
                    size = request.size,
                    price = selectedPrice,
                    qxp = selectedPrice
                )
                selectedItems.add(newItem)
            } else {
                Toast.makeText(context, "Price not found for that size", Toast.LENGTH_SHORT).show()
            }
            lookupRequest = null
        }
//        else if (item == null && request != null) {
//            Toast.makeText(context, "Item not found in menu", Toast.LENGTH_SHORT).show()
//            lookupRequest = null
//        }
    }

    Box(modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .background(color = Color.White)
                    .fillMaxHeight()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .weight(3f)
                            .background(color = Color.Gray)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val customerName = customer?.name
                            if (customerName != null) {
                                Text(color = Color.Blue, text = customerName)
                            } else {
                                Text(color = Color.Red, text = "No Customer")
                            }
                            LazyColumn {
                                itemsIndexed(selectedItems) { index, item ->
                                    val currentPrice = item.price
                                    Row {
                                        Text(
                                            color = Color.Black,
                                            text = "${item.name} (${item.size}) x${item.quantity} - Rs.${item.price * item.quantity}"
                                        )
                                        Row {
                                            Button(onClick = {
                                                if (item.quantity > 1) {
                                                    selectedItems[index] =
                                                        item.copy(
                                                            quantity = item.quantity - 1,
                                                            qxp = item.price * (item.quantity - 1)
                                                        )
                                                } else if (item.quantity == 1) {
                                                    selectedItems.removeAt(index)
                                                }
                                            }) { Text("-") }

                                            Text("${item.quantity}")

                                            Button(onClick = {
                                                selectedItems[index] =
                                                    item.copy(
                                                        quantity = item.quantity + 1,
                                                        qxp = item.price * (item.quantity + 1)
                                                    )
                                            }) { Text("+") }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(color = Color.White)
                    ) {
                        var totalPrice = 0
                        selectedItems.forEach { item ->
                            totalPrice += item.qxp
                        }
                        Text(color = Color.Black, text = "Total Price: Rs.${totalPrice}")
                    }
                }

            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .background(color = Color.DarkGray)
                    .fillMaxHeight()
            ) {
                Column(modifier = Modifier.padding(horizontal = 200.dp, vertical = 100.dp)) {
                    Text(color = Color.White, text = "Enter Menu Number")
                    TextField(value = input, onValueChange = { input = it })
                    Numpad { digit ->
                        when (digit) {
                            "<-" -> input = ""
                            "S", "M", "L" -> {
                                if (input.isNotEmpty()) {
                                    lookupRequest = LookupRequest(input.toInt(), digit)
                                    //pendingSize = digit
                                    input = ""
                                }
                            }

                            else -> input += digit
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun Items() {

}

@Preview
@Composable
fun OrderDisplay() {

}


