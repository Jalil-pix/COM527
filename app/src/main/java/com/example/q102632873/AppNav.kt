package com.example.q102632873

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.q102632873.database.PoiDatabaseHelper
import com.example.q102632873.screens.AddPoiScreen
import com.example.q102632873.screens.MapScreen
import com.example.q102632873.screens.PoiListScreen
import com.example.q102632873.screens.SearchPoiScreen
import com.example.q102632873.viewmodel.PoiViewModel

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val poiViewModel: PoiViewModel = viewModel()

    val context = LocalContext.current
    val dbHelper = remember {
        PoiDatabaseHelper(context)
    }

    LaunchedEffect(Unit) {
        poiViewModel.loadPois(dbHelper.getAllPois())
    }

    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable("map") {
            MapScreen(
                poiViewModel = poiViewModel,
                onAddPoiClick = {
                    navController.navigate("add")
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }

        composable("add") {
            AddPoiScreen(
                poiViewModel = poiViewModel,
                dbHelper = dbHelper,
                onPoiAdded = {
                    navController.popBackStack()
                }
            )
        }

        composable("search") {
            SearchPoiScreen(
                poiViewModel = poiViewModel,
                dbHelper = dbHelper,
                onSearchComplete = {
                    navController.navigate("map")
                }
            )
        }

        composable("list") {
            PoiListScreen()
        }
    }
}