package com.example.q102632873.data

data class PointOfInterest(
    val id: Int = 0,
    val name: String,
    val type: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    val code: String = "0x4d4144"
)