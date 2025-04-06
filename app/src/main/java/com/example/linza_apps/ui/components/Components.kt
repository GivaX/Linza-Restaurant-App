package com.example.linza_apps.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.linza_apps.navigation.Screen
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Linza Takeaway & Home Delivery",
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true

                    }
                }
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        windowInsets = WindowInsets(0, 15, 0, 0)
    )
}

@Composable
fun Tabs(navController: NavController, i: Int, modifier: Modifier = Modifier) {
    //val isHomeScreen = navController.currentBackStackEntryAsState().value?.destination?.route == Screen.Home.route
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(Screen.NewOrder, Screen.Menu, Screen.Delivery, Screen.Customers)

    TabRow(
        selectedTabIndex = //if (isHomeScreen) -1 else
            selectedTabIndex,
        indicator = { tabPositions ->
            if (i != -1) {
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[i]),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        tabs.forEachIndexed { index, screen ->
            Tab(
                selected = //if (isHomeScreen) false else
                    selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(screen.route) {
                        popUpTo(screen.route) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.padding(all = 10.dp),
                content = {
                    Text(
                        text = screen.route.replace("_", " ")
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
//                    color = if (!isHomeScreen && selectedTabIndex == i) {
//                        MaterialTheme.colorScheme.primary
//                    } else {
//                        MaterialTheme.colorScheme.onSurface//.copy(alpha = 0.6f)
//                    }
                    )
                }

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
fun CustomSearchBar(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Customer LookUp") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        Button(onClick = {
            // Process the text
        }) {
            Text("Search")
        }
    }
}