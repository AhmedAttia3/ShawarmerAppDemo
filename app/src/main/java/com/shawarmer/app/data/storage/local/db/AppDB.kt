package com.shawarmer.app.data.storage.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shawarmer.app.data.model.DishModel

@Database(
    entities = [
        DishModel::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDB : RoomDatabase() {
    abstract fun channelDao(): AppDao
}