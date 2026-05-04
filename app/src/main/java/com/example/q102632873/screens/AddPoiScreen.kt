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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.q102632873.viewmodel.PoiViewModel

@Composable
fun AddPoiScreen(
    poiViewModel: PoiViewModel,
    onPoiAdded: () -> Unit
) {
    val currentLat by poiViewModel.currentLat.collectAsState()
    val currentLon by poiViewModel.currentLon.collectAsState()

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("0x4d4144") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Add Point of Interest",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Current location: $currentLat, $currentLon",
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type (e.g. pub, hotel, restaurant)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("POI Code") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                val success = poiViewModel.addPoi(
                    name = name,
                    type = type,
                    description = description,
                    code = code
                )

                if (success) {
                    onPoiAdded()
                } else {
                    errorMessage =
                        "All fields required. Code must start with 0x4d4144."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add POI")
        }
    }
}
