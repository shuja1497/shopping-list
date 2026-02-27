package dev.zenolabs.groceryshopping.data.repository

import dev.zenolabs.groceryshopping.data.local.ShoppingItem
import dev.zenolabs.groceryshopping.data.local.ShoppingItemDao
import dev.zenolabs.groceryshopping.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val dao: ShoppingItemDao,
) : ShoppingRepository {

    override fun getAllItems(): Flow<List<ShoppingItem>> {
        return dao.getAllItems()
    }

    override suspend fun addItem(item: ShoppingItem) {
        dao.insertItem(item)
    }

    override suspend fun updateItem(item: ShoppingItem) {
        dao.updateItem(item)
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        dao.deleteItem(item)
    }
}
