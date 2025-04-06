package com.example.linza_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.ui.components.*


@Composable
fun NewOrderScreen(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            Column {
                AppBar(navController)
                Tabs(navController, 0)
            }

        },
        //contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { innerPadding ->
            NewOrderContent(Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun NewOrderContent(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.linza_background), // Replace with your PNG
            contentDescription = "Background",
            contentScale = ContentScale.Fit, // Choose how the image fills the space
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF0D111C))
                .graphicsLayer(alpha = 0.2f)
        )
        Column {
            DateTimeDisplay()
            CustomerLookup()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomerLookup(modifier: Modifier = Modifier) {
    val colors1 = SearchBarDefaults.colors()
    var text = remember { mutableStateOf("") }
    var active = remember { mutableStateOf(false) }
    Box(
        modifier
            .fillMaxWidth()
            .padding(top = 100.dp), contentAlignment = Alignment.Center
    ) {
        Row {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text.value,
                        onQueryChange = {
                            text.value = it
                        },
                        onSearch = { active.value = false },
                        expanded = active.value,
                        onExpandedChange = { active.value = false },
                        enabled = true,
                        placeholder = { Text(text = "Customer Lookup") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Search Icon"
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        colors = colors1.inputFieldColors,
                        interactionSource = null,
                    )
                },
                expanded = active.value,
                onExpandedChange = { active.value = true },
                modifier = Modifier
//                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                shape = SearchBarDefaults.inputFieldShape,
                colors = colors1,
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                windowInsets = SearchBarDefaults.windowInsets,
                content = {},
            )
            //Spacer(modifier = Modifier.padding(all = 50.dp))
            Button(onClick = {

            },) {
                Text("Add New Customer")
            }
            Spacer(modifier = Modifier.padding(all = 50.dp))
            CustomSearchBar()
        }
    }
}