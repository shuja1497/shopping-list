package com.droid.shopping.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ShoppingItem::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(CategoryConverter::class)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}
