package com.example.q102632873.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.q102632873.database.PoiDatabaseHelper
import com.example.q102632873.viewmodel.PoiViewModel

@Composable
fun SearchPoiScreen(
    poiViewModel: PoiViewModel,
    dbHelper: PoiDatabaseHelper,
    onSearchComplete: () -> Unit
) {
    var type by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Search POIs by Type",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type e.g. pub, restaurant, hotel") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val results = dbHelper.searchPoisByType(type)
                poiViewModel.showSearchResults(results)

                message = "Found ${results.size} result(s)"

                if (results.isNotEmpty()) {
                    onSearchComplete()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        if (message.isNotEmpty()) {
            Text(message)
        }
    }
}