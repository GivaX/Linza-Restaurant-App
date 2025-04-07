package com.example.linza_apps.ui.screens

import android.icu.text.StringSearch
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.linza_apps.R
import com.example.linza_apps.navigation.Screen
import com.example.linza_apps.ui.components.*
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
            NewOrderContent(navController, Modifier.padding(innerPadding))
        }
    )
}

@Composable
fun NewOrderContent(navController: NavController, modifier: Modifier = Modifier) {
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
            CustomerLookup(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarBox(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    //val results = searchResults.value
    Box(
        //modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                //.align(Alignment.Center)
                 .padding(horizontal = 100.dp)
            ,
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit{ replace(0,length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        //Log.e("Firestore", searchResults.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it},
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it},
        )
        {
            Column(
                Modifier
                    .verticalScroll((rememberScrollState()))
                    .clip(RoundedCornerShape(25.dp))
            ) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                                navController.navigate(Screen.Menu.route)
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

}



@Composable
fun CustomerLookup(navController: NavController, modifier: Modifier = Modifier) {
    val textFieldState = rememberTextFieldState()
//    val items = listOf(
//        "customer 1", "sldkmfklsd", "metorsdkfs"
//    )
//
//    val filteredItems by remember {
//        derivedStateOf {
//            val searchText = textFieldState.text.toString()
//            if (searchText.isEmpty()) {
//                emptyList()
//            } else {
//                items.filter { it.contains(searchText, ignoreCase = true)}
//            }
//        }
//    }
    val searchResults = remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()
    Box(
        modifier
            .fillMaxSize()
            ,
    ) {
        SearchBarBox(
            textFieldState = textFieldState,
            onSearch =  { query ->
                scope.launch {
                    handleSearch(query, searchResults)
                }
                //Log.e("Firestore", searchResults.value.toString())
            },
            searchResults = searchResults.value,
            navController = navController,
            modifier.align(Alignment.Center)
        )
        /*Row {
            Button(onClick = {

            },) {
                Text("Add New Customer")
            }
        }*/
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddCustomers.route) {
                    popUpTo(Screen.AddCustomers.route) {
                        saveState = true
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Filled.Add, "Add") },
            text = { Text("Add New Customer") },
            modifier = Modifier
                .padding(all = 15.dp)
                .align(alignment = Alignment.BottomEnd)
        )
    }
}

suspend fun handleSearch(
    query: String,
    searchResults: MutableState<List<String>>
) {
    val db = Firebase.firestore

    val customerRef = db.collection("Customers")

    try {
        val querySnapshot = customerRef
            .whereGreaterThanOrEqualTo("name", query)
            .limit(10)
            .get()
            .await()

        Log.e("Firestore", "Query: ${querySnapshot.documents.toString()}")

        val customerNames = querySnapshot.documents.mapNotNull { document ->
            document.getString("name")
        }
        Log.e("Firestore", "Customers names: ${customerNames.toString()}")
        searchResults.value = customerNames
    } catch (e:Exception) {
        Log.e("Firestore", "Error searching customers", e)
    }
}