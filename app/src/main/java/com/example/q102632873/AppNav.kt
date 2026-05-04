package com.example.q102632873

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.q102632873.screens.AddPoiScreen
import com.example.q102632873.screens.MapScreen
import com.example.q102632873.screens.PoiListScreen
import com.example.q102632873.screens.SearchPoiScreen
import com.example.q102632873.viewmodel.PoiViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()

    val poiViewModel: PoiViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "map"
    ) {

        // 🔹 MAP SCREEN
        composable("map") {
            MapScreen(
                poiViewModel = poiViewModel,
                onAddPoiClick = {
                    navController.navigate("add")
                }
            )
        }
        composable("add") {
            AddPoiScreen(
                poiViewModel = poiViewModel,
                onPoiAdded = {
                    navController.popBackStack() // go back to map
                }
            )
        }
        composable("search") {
            SearchPoiScreen()
        }
        composable("list") {
            PoiListScreen()
        }
    }
}