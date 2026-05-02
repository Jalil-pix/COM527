package com.example.q102632873

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.q102632873.ui.theme.COM527Theme
import com.example.q102632873.AppNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            COM527Theme {
                AppNav()
            }
        }
    }
}