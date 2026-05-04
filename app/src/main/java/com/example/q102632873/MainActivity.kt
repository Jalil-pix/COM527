package com.example.q102632873

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.q102632873.ui.theme.COM527Theme
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(
            this,
            "",
            WellKnownTileServer.MapLibre
        )

        setContent {
            COM527Theme {
                AppNav()
            }
        }
    }
}
