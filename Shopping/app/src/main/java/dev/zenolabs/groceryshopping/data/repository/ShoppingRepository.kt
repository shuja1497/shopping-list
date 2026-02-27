package dev.zenolabs.groceryshopping.data.repository

import dev.zenolabs.groceryshopping.data.local.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    fun getAllItems(): Flow<List<ShoppingItem>>

    suspend fun addItem(item: ShoppingItem)

    suspend fun updateItem(item: ShoppingItem)

    suspend fun deleteItem(item: ShoppingItem)
}