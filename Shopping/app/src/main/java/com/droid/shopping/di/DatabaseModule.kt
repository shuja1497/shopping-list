package com.droid.shopping.di

import android.content.Context
import androidx.room.Room
import com.droid.shopping.data.local.ShoppingDatabase
import com.droid.shopping.data.local.ShoppingItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShoppingDatabase =
        Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "shopping_db",
        ).build()

    @Provides
    @Singleton
    fun provideShoppingItemDao(database: ShoppingDatabase): ShoppingItemDao =
        database.shoppingItemDao()
}
