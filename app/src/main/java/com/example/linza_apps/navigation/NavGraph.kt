package com.example.linza_apps.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linza_apps.ui.screens.AddCustomerScreen
import com.example.linza_apps.ui.screens.HomeScreen
import com.example.linza_apps.ui.screens.MenuScreen
import com.example.linza_apps.ui.screens.NewOrderScreen
import com.example.linza_apps.ui.screens.DeliveryScreen
import com.example.linza_apps.ui.screens.CustomerScreen
import com.example.linza_apps.ui.screens.DriverScreen


sealed class Screen(val route:String) {
    object Home : Screen("home")
    object NewOrder : Screen("new_order")
    object Menu : Screen("menu")
    object Delivery : Screen("delivery")
    object Customers : Screen("customers")
    object Drivers : Screen("drivers")
    object AddCustomers : Screen("add_customers")
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Home.route){
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.NewOrder.route) {NewOrderScreen(navController)}
        composable(Screen.Menu.route) {MenuScreen(navController)}
        composable(Screen.Delivery.route) {DeliveryScreen(navController)}
        composable(Screen.Customers.route) {CustomerScreen(navController)}
        composable(Screen.Drivers.route) {DriverScreen(navController)}
        composable(Screen.AddCustomers.route) { AddCustomerScreen(navController) }
    }
}
