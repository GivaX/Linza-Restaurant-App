package com.example.linza_apps


import kotlinx.coroutines.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.linza_apps.ui.theme.AppTheme
import com.example.linza_apps.navigation.AppNavigation
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            AppTheme {
                LinzaApp()
            }
        }
    }
}

@Composable
fun LinzaApp(modifier: Modifier = Modifier) {
    Places.initialize(LocalContext.current, BuildConfig.MAPS_API_KEY)
    AppNavigation()
}

/*@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Linza Takeaway & Home Delivery"
                        )
                    },
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    windowInsets = WindowInsets(0, 15, 0, 0)
                )
                Tabs()
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            DashboardContent(Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun DashboardContent(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.linza_background), // Replace with your PNG
            contentDescription = "Background",
            contentScale = ContentScale.Fit, // Choose how the image fills the space
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF0E1522))
        )
        Column {
            DateTimeDisplay()
            ManageDriversButton()
        }
    }

}

@Preview
@Composable
fun Tabs(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("New Order", "Menu", "Delivery", "Customers")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                modifier = Modifier.padding(all = 10.dp),
                content = { Text(text = title) }

            )
        }
    }
}

@Preview
@Composable
fun DateTimeDisplay(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime = getCurrentTime()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Text(
            text = currentTime,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss a", Locale.getDefault())
    return sdf.format(Date())
}

@Preview
@Composable
fun ManageDriversButton(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Button(
            onClick = {
                //Nav to manage drivers screen
            },
        ) {
            Text("Manage Drivers")
        }
    }

}


*/