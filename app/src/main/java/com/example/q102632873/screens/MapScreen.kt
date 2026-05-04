package com.example.q102632873.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.q102632873.viewmodel.PoiViewModel
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.ramani.compose.CameraPosition
import org.ramani.compose.MapLibre
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

@Composable
fun MapScreen(
    poiViewModel: PoiViewModel,
    onAddPoiClick: () -> Unit
) {
    val context = LocalContext.current

    var currentLocation by remember {
        mutableStateOf(LatLng(50.902614, -1.404464))
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasLocationPermission =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (hasLocationPermission) {
        DisposableEffect(Unit) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    currentLocation = LatLng(location.latitude, location.longitude)

                    poiViewModel.updateCurrentLocation(
                        location.latitude,
                        location.longitude
                    )
                }
            }

            if (
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    2f,
                    listener
                )

                val lastLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (lastLocation != null) {
                    currentLocation = LatLng(lastLocation.latitude, lastLocation.longitude)

                    poiViewModel.updateCurrentLocation(
                        lastLocation.latitude,
                        lastLocation.longitude
                    )
                }
            }

            onDispose {
                locationManager.removeUpdates(listener)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        MapLibre(
            modifier = Modifier.fillMaxSize(),
            styleBuilder = Style.Builder().fromUri(
                "https://demotiles.maplibre.org/style.json"
            ),
            cameraPosition = CameraPosition(
                target = currentLocation,
                zoom = 15.0
            )
        )

        Column {
            Text(
                text = "Lat: ${currentLocation.latitude}, Lon: ${currentLocation.longitude}"
            )

            Button(onClick = onAddPoiClick) {
                Text("Add POI")
            }
        }
    }
}