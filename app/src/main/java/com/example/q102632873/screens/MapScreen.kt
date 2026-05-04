package com.example.q102632873.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.q102632873.data.PointOfInterest
import com.example.q102632873.viewmodel.PoiViewModel
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.annotations.MarkerOptions

@Composable
fun MapScreen(
    poiViewModel: PoiViewModel,
    onAddPoiClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current
    val poiList by poiViewModel.poiList.collectAsState()

    var selectedPoi by remember {
        mutableStateOf<PointOfInterest?>(null)
    }

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

    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
            onStart()
            onResume()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    Box {
        AndroidView(
            factory = { mapView },
            update = { view ->
                view.getMapAsync { map ->
                    map.setStyle(
                        Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")
                    ) {

                        map.cameraPosition = CameraPosition.Builder()
                            .target(currentLocation)
                            .zoom(15.0)
                            .build()

                        map.clear()

                        poiList.forEach { poi ->
                            map.addMarker(
                                MarkerOptions()
                                    .position(LatLng(poi.lat, poi.lon))
                                    .title(poi.name)
                                    .snippet("${poi.type}: ${poi.description}")
                            )
                        }

                        map.setOnMarkerClickListener { marker ->
                            selectedPoi = poiList.find {
                                it.name == marker.title &&
                                        it.lat == marker.position.latitude &&
                                        it.lon == marker.position.longitude
                            }
                            true
                        }
                    }
                }
            }
        )

        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Lat: ${currentLocation.latitude}, Lon: ${currentLocation.longitude}"
            )

            Button(onClick = onAddPoiClick) {
                Text("Add POI")
            }

            Button(onClick = onSearchClick) {
                Text("Search POI")
            }
        }
    }

    if (selectedPoi != null) {
        AlertDialog(
            onDismissRequest = {
                selectedPoi = null
            },
            title = {
                Text(selectedPoi!!.name)
            },
            text = {
                Column {
                    Text("Type: ${selectedPoi!!.type}")
                    Text("Description: ${selectedPoi!!.description}")
                    Text("Code: ${selectedPoi!!.code}")
                    Text("Lat: ${selectedPoi!!.lat}")
                    Text("Lon: ${selectedPoi!!.lon}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedPoi = null
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }
}