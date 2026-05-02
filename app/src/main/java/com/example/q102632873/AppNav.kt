package com.example.q102632873

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.q102632873.screens.AddPoiScreen
import com.example.q102632873.screens.MapScreen
import com.example.q102632873.screens.PoiListScreen
import com.example.q102632873.screens.SearchPoiScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable("map") {
            MapScreen()
        }

        composable("add") {
            AddPoiScreen()
        }

        composable("search") {
            SearchPoiScreen()
        }

        composable("list") {
            PoiListScreen()
        }
    }
}