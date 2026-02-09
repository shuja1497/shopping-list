package com.droid.shopping.data.local

import androidx.room.TypeConverter

class CategoryConverter {

    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    @TypeConverter
    fun toCategory(name: String): Category = Category.fromName(name)
}
