package com.example.linza_apps.ui.components

import android.location.Address
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linza_apps.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firestore.v1.StructuredQuery.Order
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
                        text = when {
                            screen.route.startsWith("menu") -> "Menu"
                            else -> screen.route.replace("_", " ")
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                        },
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

fun getTimeForOrder(): String {
    val date = LocalDateTime.now()
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"))
}

@Composable
fun CustomerLookup(navController: NavController,flag: String, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
        ,
    ) {
        CustomerSearchBar(navController = navController, flag = flag)
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

@Composable
fun CustomerSearchBar(viewModel: CustomerViewModel = viewModel(), navController: NavController, flag: String, modifier: Modifier = Modifier) {
    val searchQuery by remember { mutableStateOf(viewModel.searchQuery) }
    val results by viewModel.searchResults

    Column(modifier = Modifier.padding(75.dp)) {
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearchChange(it)},
            label = { Text("Search Customers") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        //Spacer(Modifier.height(10.dp))

        if (flag == "Menu") {
            LazyColumn {
                items(results) { customer ->
                    Column (
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("menu/${customer.id}") }
                    ){
                        Text(
                            "${customer.name} - ${customer.phone}",
                        )
                        Log.e("Debug", "$customer.name = $customer.phone")
                    }
                }
            }
        } else if (flag == "Customers") {
            LazyColumn {
                items(results) { customer ->
                    Column (
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("view_customer/${customer.id}") }
                    ){
                        Text(
                            "${customer.name} - ${customer.phone}",
                        )
                        Log.e("Debug", "$customer.name = $customer.phone")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EnterDetails() {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        SnackbarHost(hostState = snackbarHostState, Modifier.align(Alignment.BottomEnd))
        Column {
            CustomTextField(name, "Name", { newText -> name = newText })
            //Spacer(modifier = Modifier.padding(all = 10.dp))
            CustomTextField(phoneNumber, "Phone Number", { newText -> phoneNumber = newText })
            //Spacer(modifier = Modifier.padding(all = 10.dp))
            CustomTextField(address, "Address", { newText -> address = newText })
            Button(onClick = {

                val customerRef = db.collection("Customers").document()

                val customer = hashMapOf(
                    "id" to customerRef.id,
                    "name" to name.trim().lowercase(),
                    "phone" to phoneNumber,
                    "address" to address
                )
                name = ""
                phoneNumber = ""
                address = ""

                customerRef.set(customer)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Customer added w ID: ${customerRef.id}")
                        scope.launch {
                            snackbarHostState.showSnackbar("Customer Added Successfully")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding customer", e)
                        scope.launch {
                            snackbarHostState.showSnackbar("Customer Addition Failed")
                        }
                    }
            }) {
                Text("Add Customer")
            }
        }
    }
}

data class MenuItem(
    val menuNumber: Int = 0,
    val name: String = "",
    val category: String = "",
    val priceSmall: Int? = null,
    val priceMedium: Int? = null,
    val priceLarge: Int? = null,
    val available: Boolean = true
)

data class OrderItem(
    val menuNumber: Int = 0,
    val name: String = "",
    val size: String = "",
    val price: Int = 0,
    val qxp: Int = 0, //Quantity * Price for calculations
    val quantity: Int = 1
)

@Composable
fun CustomTextField(text: String, label:String, textStateChanged: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = { newText -> textStateChanged(newText)},
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.padding(all = 15.dp)
    )
}

data class Customer (
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = ""
)

data class OrderItemsList(
    val date: String = "",
    val items: List<OrderItem> = emptyList(),
    val total: Int = 0
)

class CustomerViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
    var searchResults = mutableStateOf<List<Customer>>(emptyList())
    private val db = Firebase.firestore

    fun onSearchChange(query: String){
        searchQuery = query
        if (query.isNotEmpty()){
            searchCustomers(query)
        } else {
            searchResults.value = emptyList()
        }
    }

    private fun searchCustomers(query: String) {
        db.collection("Customers")
            .orderBy("name")
            .startAt(query.lowercase())
            .endAt(query.lowercase() + "\uf8ff")
            .get()
            .addOnSuccessListener { snapshot ->
                val customers = snapshot.documents.mapNotNull {
                    it.toObject(Customer::class.java)
                }
                searchResults.value = customers
            }
    }

    fun getCustomerId(customerId : String): Flow<Customer?> = flow {
        val snapshot = db.collection("Customers")
            .document(customerId)
            .get()
            .await()
        emit(snapshot.toObject(Customer::class.java))
    }

    fun getCustomerOrders(customerId: String): Flow<List<OrderItemsList>> = flow {
        val snapshot = db.collection("Customers")
            .document(customerId)
            .collection("Orders")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()

        val orders = snapshot.documents.mapNotNull { doc ->
            try {
                val order = doc.toObject(OrderItemsList::class.java)
                order
            } catch (e: Exception) {
                null
            }
        }

        emit(orders)
    }
}

@Composable
fun Numpad(onDigit: (String) -> Unit) {
    Column {
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0","<-", "S", "M", "L").chunked(3).forEach { row ->
            Row {
                row.forEach { digit ->
                    Button(modifier = Modifier.padding(horizontal = 3.dp), onClick = { onDigit(digit) }) {
                        Text(digit)
                    }
                }
            }
        }
    }
}

class MenuViewModel : ViewModel() {
    private val db = Firebase.firestore

    val _menuItem = MutableStateFlow<MenuItem?>(null)
    val menuItem : StateFlow<MenuItem?> = _menuItem

    fun fetchItem(menuNum : Int) {
        db.collection("Menu")
            .whereEqualTo("menuNumber", menuNum)
            .get()
            .addOnSuccessListener { snapshot ->
                val item = snapshot.documents.firstOrNull()?.toObject(MenuItem::class.java)
                _menuItem.value = item
            }
    }

    fun fetchMenu(): Flow<List<MenuItem>> = flow {

        val menuRef = Firebase.firestore
            .collection("Menu")
            .get()
            .await()
        val items = menuRef.documents.mapNotNull { doc ->
            try {
                val item = doc.toObject(MenuItem::class.java)
                item
            } catch (e: Exception) {
                null
            }
        }
        emit(items)
    }
}