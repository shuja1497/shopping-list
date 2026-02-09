package com.droid.shopping.data.repository

import app.cash.turbine.test
import com.droid.shopping.data.local.Category
import com.droid.shopping.data.local.ShoppingItem
import com.droid.shopping.data.local.ShoppingItemDao
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ShoppingRepositoryImplTest {

    private lateinit var repository: ShoppingRepositoryImpl
    private lateinit var dao: ShoppingItemDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = ShoppingRepositoryImpl(dao)
    }

    @Test
    fun `getAllItems returns flow from dao`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Milk", category = Category.MILK),
            ShoppingItem(id = 2, name = "Bread", category = Category.BREADS),
        )
        every { dao.getAllItems() } returns flowOf(items)

        repository.getAllItems().test {
            assertThat(awaitItem()).isEqualTo(items)
            awaitComplete()
        }
    }

    @Test
    fun `addItem delegates to dao insertItem`() = runTest {
        val item = ShoppingItem(name = "Apples", category = Category.FRUITS)

        repository.addItem(item)

        coVerify { dao.insertItem(item) }
    }

    @Test
    fun `updateItem delegates to dao updateItem`() = runTest {
        val item = ShoppingItem(id = 1, name = "Milk", category = Category.MILK, isPurchased = true)

        repository.updateItem(item)

        coVerify { dao.updateItem(item) }
    }

    @Test
    fun `deleteItem delegates to dao deleteItem`() = runTest {
        val item = ShoppingItem(id = 1, name = "Bread", category = Category.BREADS)

        repository.deleteItem(item)

        coVerify { dao.deleteItem(item) }
    }

    @Test
    fun `addItem propagates dao exception`() = runTest {
        val item = ShoppingItem(name = "Steak", category = Category.MEATS)
        coEvery { dao.insertItem(any()) } throws RuntimeException("Insert failed")

        val result = runCatching { repository.addItem(item) }

        assertThat(result.isFailure).isTrue()
    }
}
