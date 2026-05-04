package com.example.q102632873.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.q102632873.data.PointOfInterest

class PoiDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "points_of_interest.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE points_of_interest (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                description TEXT NOT NULL,
                lat REAL NOT NULL,
                lon REAL NOT NULL,
                code TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS points_of_interest")
        onCreate(db)
    }

    // 🔹 Insert POI
    fun insertPoi(poi: PointOfInterest): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("id", poi.id)
            put("name", poi.name)
            put("type", poi.type)
            put("description", poi.description)
            put("lat", poi.lat)
            put("lon", poi.lon)
            put("code", poi.code)
        }

        return db.insert("points_of_interest", null, values)
    }

    // 🔹 Get ALL POIs
    fun getAllPois(): List<PointOfInterest> {
        val db = readableDatabase
        val pois = mutableListOf<PointOfInterest>()

        val cursor = db.rawQuery(
            "SELECT id, name, type, description, lat, lon, code FROM points_of_interest",
            null
        )

        while (cursor.moveToNext()) {
            val poi = PointOfInterest(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                type = cursor.getString(2),
                description = cursor.getString(3),
                lat = cursor.getDouble(4),
                lon = cursor.getDouble(5),
                code = cursor.getString(6)
            )
            pois.add(poi)
        }

        cursor.close()
        return pois
    }

    // 🔹 SEARCH POIs BY TYPE (Task 5)
    fun searchPoisByType(type: String): List<PointOfInterest> {
        val db = readableDatabase
        val pois = mutableListOf<PointOfInterest>()

        val cursor = db.rawQuery(
            "SELECT id, name, type, description, lat, lon, code FROM points_of_interest WHERE LOWER(type) LIKE LOWER(?)",
            arrayOf("%$type%")
        )

        while (cursor.moveToNext()) {
            pois.add(
                PointOfInterest(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    type = cursor.getString(2),
                    description = cursor.getString(3),
                    lat = cursor.getDouble(4),
                    lon = cursor.getDouble(5),
                    code = cursor.getString(6)
                )
            )
        }

        cursor.close()
        return pois
    }
}