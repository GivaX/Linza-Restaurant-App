package com.example.linza_apps.ui.components

import android.app.AlertDialog
import android.location.Address
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linza_apps.navigation.Screen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firestore.v1.StructuredQuery.Order
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
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
fun CustomerLookup(navController: NavController, flag: String, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier
            .fillMaxSize(),
    ) {
        CustomerSearchBar(navController = navController, flag = flag)
        ExtendedFloatingActionButton(
            onClick = {
                showDialog = true
//                navController.navigate(Screen.AddCustomers.route) {
//                    popUpTo(Screen.AddCustomers.route) {
//                        saveState = true
//                        inclusive = true
//                    }
//                    launchSingleTop = true
//                }
            },
            icon = { Icon(Icons.Filled.Add, "Add") },
            text = { Text("Add New Customer") },
            modifier = Modifier
                .padding(all = 15.dp)
                .align(alignment = Alignment.BottomEnd)
        )
        AddCustomerDialog(
            showDialog = showDialog,
            onDismissRequest = {
                showDialog = false
            })
    }
}

@Composable
fun AddCustomerDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var long by remember { mutableDoubleStateOf(0.0) }
    //var latLng by remember { mutableStateOf(LatLng(0.0,0.0)) }
    val context = LocalContext.current

    val db = FirebaseFirestore.getInstance()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
                name = ""
                phoneNumber = ""
                address = ""},
            title = { Text("Add a New Customer") },
            text = {
                Column {
                    CustomTextField(name, "Name", { newText -> name = newText })
                    CustomTextField(
                        phoneNumber,
                        "Phone Number",
                        { newText -> phoneNumber = newText })
                    LocationSearchField(
                        address = address,
                        onQueryChange = { address = it },
                        onPlaceSelected = { selected ->
                            if (selected != null) {
                                lat = selected.latitude
                                long = selected.longitude
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val customerRef = db.collection("Customers").document()

                    val customer = hashMapOf(
                        "id" to customerRef.id,
                        "name" to name.trim().lowercase(),
                        "phone" to phoneNumber,
                        "address" to address,
                        "lat" to lat,
                        "long" to long
                    )
                    name = ""
                    phoneNumber = ""
                    address = ""

                    customerRef.set(customer)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Customer added w ID: ${customerRef.id}")
                            onDismissRequest()
                            Toast.makeText(
                                context,
                                "Customer Added Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error adding customer", e)
                            onDismissRequest()
                            Toast.makeText(
                                context,
                                "Error Adding Customer",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                }) { Text("Add Customer") }
            },
            dismissButton = { TextButton(onClick = {
                onDismissRequest()
                name = ""
                phoneNumber = ""
                address = ""
            }) { Text("Cancel") } }
        )
    }
}

@Composable
fun CustomerSearchBar(
    viewModel: CustomerViewModel = viewModel(),
    navController: NavController,
    flag: String,
    modifier: Modifier = Modifier
) {
    val searchQuery by remember { mutableStateOf(viewModel.searchQuery) }
    val results by viewModel.searchResults

    Column(modifier = Modifier.padding(75.dp)) {
        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearchChange(it) },
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
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("menu/${customer.id}") }
                    ) {
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
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("view_customer/${customer.id}") }
                    ) {
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

@Composable
fun LocationSearchField(
    modifier: Modifier = Modifier,
    address: String,
    onQueryChange: (String) -> Unit,
    onPlaceSelected: (LatLng?) -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }

    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // 1st APPROACH

    LaunchedEffect(address) {
        if (address.length > 2) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(address)
                .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    predictions = response.autocompletePredictions
                    Log.e("Maps", "locations found\n${predictions}")
                    expanded = true
                }
                .addOnFailureListener { response ->
                    predictions = emptyList()
                    Log.e("Maps", "locations not found?\n${predictions}", response)
                    expanded = false
                }
        } else {
            predictions = emptyList()
            expanded = false
        }
    }

    Box() {
        Column() {
            TextField(
                value = address,
                onValueChange = onQueryChange,
                label = { Text("Address") },
                singleLine = true,
                modifier = Modifier
                    .zIndex(2f)
                    .width(280.dp)
            )
            if (expanded) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(0, 115),
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .zIndex(1f)
                            .padding(horizontal = 4.dp)
                            .animateContentSize()
                            .width(275.dp)
                    ) {
                        items(predictions.take(3)) { prediction ->

                            Text(
                                modifier = Modifier
                                    .clickable {
                                        val selectedLoc = prediction.getFullText(null).toString()
                                        onQueryChange(selectedLoc)

                                        val placeId = prediction.placeId
                                        val placeFields = listOf(
                                            Place.Field.ID,
                                            Place.Field.DISPLAY_NAME,
                                            Place.Field.LOCATION,
                                            Place.Field.FORMATTED_ADDRESS
                                        )
                                        val fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields)


                                        placesClient.fetchPlace(fetchPlaceRequest)
                                            .addOnSuccessListener { response ->
                                                val place = response.place
                                                Log.d("PlacesAPI", "Place details fetched: ${place.formattedAddress}, \nLatLng: ${place.location}, \nname: ${place.displayName}, \naddress:${selectedLoc}")
                                                onPlaceSelected(place.location)
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("PlacesAPI", "Failed to fetch place details", exception)
                                                onQueryChange("")
                                            }

                                        expanded = false
                                        focusManager.clearFocus()
                                        predictions = emptyList()
                                    }
                                    .padding(12.dp),
                                text = prediction.getFullText(null).toString(),
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}

//vvvvvvvvvvvvvvvvvvvvv
// OUT OF SCOPE FEATURE
//vvvvvvvvvvvvvvvvvvvvv

// Google Map Display and Location Picker Test Composable

@Composable
fun LocationPickerMap(
    initialLatLng: LatLng = LatLng(6.9271, 79.8612), // Default Colombo
    onLocationSelected: (LatLng) -> Unit
) {
    var markerPosition by remember { mutableStateOf(initialLatLng) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(initialLatLng, 15f)
        },
        onMapClick = { latLng ->
            markerPosition = latLng
            onLocationSelected(latLng)
        }
    ) {
        Marker(
            state = rememberMarkerState(position = markerPosition),
            draggable = true,
            onClick = { marker ->
                markerPosition = marker.position
                onLocationSelected(marker.position)
                return@Marker false
            },
        )
    }
}

//^^^^^^^^^^^^^^^^^^^^^
// OUT OF SCOPE FEATURE
//^^^^^^^^^^^^^^^^^^^^^

@Preview
@Composable
fun EnterDetails() {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var latLng by remember { mutableStateOf<LatLng>(LatLng(6.9691122,79.9206079)) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val db = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SnackbarHost(hostState = snackbarHostState, Modifier.align(Alignment.BottomEnd))
        Column {
            CustomTextField(name, "Name", { newText -> name = newText })
            //Spacer(modifier = Modifier.padding(all = 10.dp))
            CustomTextField(phoneNumber, "Phone Number", { newText -> phoneNumber = newText })
            //Spacer(modifier = Modifier.padding(all = 10.dp))
            //CustomTextField(address, "Address", { newText -> address = newText })
            LocationSearchField(
                address = address,
                onQueryChange = { address = it },
                onPlaceSelected = { selected ->
                    if (selected != null) {
                        latLng = selected
                    }
                }
            )
            Button(onClick = {

                val customerRef = db.collection("Customers").document()

                val customer = hashMapOf(
                    "id" to customerRef.id,
                    "name" to name.trim().lowercase(),
                    "phone" to phoneNumber,
                    "address" to address,
                    "location" to latLng
                )
                name = ""
                phoneNumber = ""
                address = ""
                //latLng = ()

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
fun CustomTextField(text: String, label: String, textStateChanged: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = { newText -> textStateChanged(newText) },
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.padding(all = 15.dp)
    )
}

data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0
)

data class OrderItemsList(
    val date: String = "",
    val items: List<OrderItem> = emptyList(),
    val total: Int = 0,
    val delivery: Boolean = false
)

class CustomerViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
    var searchResults = mutableStateOf<List<Customer>>(emptyList())
    private val db = Firebase.firestore

    fun onSearchChange(query: String) {
        searchQuery = query
        if (query.isNotEmpty()) {
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

    fun getCustomerId(customerId: String): Flow<Customer?> = flow {
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

data class Driver(
    val id: String = "",
    val name: String = "",
    val phone: Long = 0,
    val deliveries: List<DeliveryOrder> = emptyList(),
    val status: Boolean = false
    //val status: String = "unassigned" // String = "assigned", "unassigned"   OR   Boolean
)

data class DeliveryOrder(
    val deliveryId: String = "",
    val customerId: String = "", // Probably not needed
    val orderId: String = "", // Probably not needed
    val customerName: String = "",
    val address: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val items: List<OrderItem> = emptyList(),
    val total: Int = 0,
    val date: String = "",
    val status: Boolean = false,
    //val status: String = "unassigned" // "assigned" "unassigned" "completed"
)

class DeliveryViewModel : ViewModel(){
    private val db = Firebase.firestore

    fun fetchDeliveries(): Flow<List<DeliveryOrder>> = callbackFlow {
        val listener = db.collection("Deliveries")
            .orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener{ snapshot, error ->
                if (error != null) {
                    Log.e("DelDri", "Listen failed", error)
                }

                val items = snapshot?.documents?.mapNotNull { doc ->
                    Log.e("DelDri", "Delivery Reference, $doc")
                    try {
                        doc.toObject(DeliveryOrder::class.java)
                    } catch (e: Exception) {
                        Log.e("DelDri", "Delivery Catch, $e")
                        null
                    }
                } ?: emptyList()

                trySend(items).isSuccess
            }
        awaitClose { listener.remove() }
    }
}

class DriverViewModel : ViewModel() {
    private val db = Firebase.firestore

    //val _drivers = MutableStateFlow<Driver?>(null)
    //val drivers: StateFlow<Driver?> = _drivers

    fun getDriverDeliveries(driverId: String): Flow<List<DeliveryOrder>> = flow {
        val snapshot = db.collection("Drivers")
            .document(driverId)
            .get()
            .await()
        snapshot.toObject(Driver::class.java)?.let { emit(it.deliveries) }
    }

    fun fetchDrivers(): Flow<List<Driver>> = callbackFlow {
        val listener = db.collection("Drivers")
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener{ snapshot, error ->
                if (error != null) {
                    Log.e("Drivers", "Listen failed", error)
                }

                val items = snapshot?.documents?.mapNotNull { doc ->
                    Log.e("Drivers", "Driver Reference, $doc")
                    try {
                        doc.toObject(Driver::class.java)
                    } catch (e: Exception) {
                        Log.e("Drivers", "Driver Catch, $e")
                        null
                    }
                } ?: emptyList()

                trySend(items).isSuccess
            }
        awaitClose { listener.remove() }
    }
}

@Composable
fun Numpad(onDigit: (String) -> Unit) {
    Column {
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "<-", "S", "M", "L").chunked(3)
            .forEach { row ->
                Row {
                    row.forEach { digit ->
                        Button(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            onClick = { onDigit(digit) }) {
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
    val menuItem: StateFlow<MenuItem?> = _menuItem

    fun fetchItem(menuNum: Int) {
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