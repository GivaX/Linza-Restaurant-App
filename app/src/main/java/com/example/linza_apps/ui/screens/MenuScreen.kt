package com.example.linza_apps.ui.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
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
fun MenuScreen(navController: NavController, customerId : String, modifier: Modifier = Modifier) {
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
    val viewModel : CustomerViewModel = viewModel()
    val MenuVM : MenuViewModel = viewModel()
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
            if (customer != null)
                customer?.let {
                    Text(it.name)
                    Menu(viewModel = MenuVM)
                }
            else {
                //Text("Testing")
                Menu(viewModel = MenuVM)
            }

            /*customer?.let {
                Text(customerId)
            }
            Text("Testing")*/

        }
    }
}

@Composable
fun Menu(modifier: Modifier = Modifier, viewModel: MenuViewModel) {
    var input by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    var selectedSize by remember { mutableStateOf<String?>(null) }

    val menuItem by viewModel.menuItem.collectAsState()

    LaunchedEffect(menuItem, selectedSize) {
        selectedSize?.let { size ->
            menuItem?.let { item ->
                itemName = item.name
                price = when (size) {
                    "S" -> item.priceSmall.toString()
                    "M" -> item.priceMedium.toString()
                    "L" -> item.priceLarge.toString()
                    else -> ""
                }
            }
        }
    }

    Box(modifier.fillMaxSize()){
        Row (Modifier.fillMaxWidth()){
            Box(modifier = Modifier
                .weight(1f)
                .background(color = Color.White)
                .fillMaxHeight()){
                Column {
                    Text(color = Color.Black, text = "$itemName $price")
                }
            }
            Box(modifier = Modifier
                .weight(3f)
                .background(color = Color.DarkGray)
                .fillMaxHeight()){
                Column(modifier = Modifier.padding(horizontal = 200.dp, vertical = 100.dp)) {
                    Text(color = Color.White,text = "Enter Menu Number")
                    TextField(value = input, onValueChange = { input = it })
                    Numpad { digit ->
                        when (digit) {
                            "<-" -> input = ""
                            "S","M","L" -> {
                                if (input.isNotEmpty()) {
                                    selectedSize = digit
                                    viewModel.fetchItem(input.toInt())
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


