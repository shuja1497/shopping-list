package com.droid.shopping.presentation

import app.cash.turbine.test
import com.droid.shopping.data.local.Category
import com.droid.shopping.data.local.ShoppingItem
import com.droid.shopping.data.repository.ShoppingRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingListViewModelTest {

    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var repository: ShoppingRepository
    private val itemsFlow = MutableStateFlow<List<ShoppingItem>>(emptyList())
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        every { repository.getAllItems() } returns itemsFlow
        viewModel = ShoppingListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() = runTest {
        val state = viewModel.uiState.value
        assertThat(state.items).isEmpty()
        assertThat(state.itemName).isEmpty()
        assertThat(state.selectedCategory).isEqualTo(Category.MILK)
        assertThat(state.filterCategory).isNull()
        assertThat(state.sortOption).isEqualTo(SortOption.DEFAULT)
        assertThat(state.editingItem).isNull()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `items from repository are observed in state`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Apples", category = Category.FRUITS),
            ShoppingItem(id = 2, name = "Bread", category = Category.BREADS),
        )
        itemsFlow.value = items

        assertThat(viewModel.uiState.value.items).isEqualTo(items)
    }

    @Test
    fun `onItemNameChanged updates item name in state`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnItemNameChanged("Milk"))
        assertThat(viewModel.uiState.value.itemName).isEqualTo("Milk")
    }

    @Test
    fun `onCategorySelected updates selected category`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnCategorySelected(Category.FRUITS))
        assertThat(viewModel.uiState.value.selectedCategory).isEqualTo(Category.FRUITS)
    }

    @Test
    fun `addItem with blank name shows error`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnItemNameChanged("   "))
        viewModel.onEvent(ShoppingListUiEvent.OnAddItemClicked)

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Item name cannot be empty")
    }

    @Test
    fun `addItem with valid name calls repository and clears input`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnItemNameChanged("Bananas"))
        viewModel.onEvent(ShoppingListUiEvent.OnCategorySelected(Category.FRUITS))
        viewModel.onEvent(ShoppingListUiEvent.OnAddItemClicked)

        coVerify {
            repository.addItem(
                ShoppingItem(name = "Bananas", category = Category.FRUITS)
            )
        }
        assertThat(viewModel.uiState.value.itemName).isEmpty()
    }

    @Test
    fun `deleteItem calls repository`() = runTest {
        val item = ShoppingItem(id = 1, name = "Milk", category = Category.MILK)

        viewModel.onEvent(ShoppingListUiEvent.OnDeleteItem(item))


        coVerify { repository.deleteItem(item) }
    }

    @Test
    fun `onFilterChanged updates filter category`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnFilterChanged(Category.VEGETABLES))
        assertThat(viewModel.uiState.value.filterCategory).isEqualTo(Category.VEGETABLES)
    }

    @Test
    fun `onSortChanged updates sort option`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnSortChanged(SortOption.ALPHABETICAL))
        assertThat(viewModel.uiState.value.sortOption).isEqualTo(SortOption.ALPHABETICAL)
    }

    @Test
    fun `filteredItems filters by category`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Apples", category = Category.FRUITS),
            ShoppingItem(id = 2, name = "Bread", category = Category.BREADS),
            ShoppingItem(id = 3, name = "Oranges", category = Category.FRUITS),
        )
        itemsFlow.value = items


        viewModel.onEvent(ShoppingListUiEvent.OnFilterChanged(Category.FRUITS))

        val filtered = viewModel.uiState.value.filteredItems
        assertThat(filtered).hasSize(2)
        assertThat(filtered.all { it.category == Category.FRUITS }).isTrue()
    }

    @Test
    fun `filteredItems sorts alphabetically`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Carrots", category = Category.VEGETABLES),
            ShoppingItem(id = 2, name = "Apples", category = Category.FRUITS),
            ShoppingItem(id = 3, name = "Bread", category = Category.BREADS),
        )
        itemsFlow.value = items


        viewModel.onEvent(ShoppingListUiEvent.OnSortChanged(SortOption.ALPHABETICAL))

        val sorted = viewModel.uiState.value.filteredItems
        assertThat(sorted.map { it.name }).containsExactly("Apples", "Bread", "Carrots").inOrder()
    }

    @Test
    fun `filteredItems sorts by status`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Milk", category = Category.MILK, isPurchased = true),
            ShoppingItem(id = 2, name = "Bread", category = Category.BREADS, isPurchased = false),
        )
        itemsFlow.value = items

        viewModel.onEvent(ShoppingListUiEvent.OnSortChanged(SortOption.STATUS))

        val sorted = viewModel.uiState.value.filteredItems
        assertThat(sorted.first().isPurchased).isFalse()
        assertThat(sorted.last().isPurchased).isTrue()
    }

    @Test
    fun `onEditConfirmed saves changes and clears edit state`() = runTest {
        val item = ShoppingItem(id = 1, name = "Milk", category = Category.MILK)
        viewModel.onEvent(ShoppingListUiEvent.OnEditItem(item))
        viewModel.onEvent(ShoppingListUiEvent.OnEditNameChanged("Almond Milk"))
        viewModel.onEvent(ShoppingListUiEvent.OnEditCategoryChanged(Category.VEGETABLES))
        viewModel.onEvent(ShoppingListUiEvent.OnEditConfirmed)


        coVerify {
            repository.updateItem(item.copy(name = "Almond Milk", category = Category.VEGETABLES))
        }
        assertThat(viewModel.uiState.value.editingItem).isNull()
    }

    @Test
    fun `onEditDismissed clears editing state`() = runTest {
        val item = ShoppingItem(id = 1, name = "Milk", category = Category.MILK)
        viewModel.onEvent(ShoppingListUiEvent.OnEditItem(item))
        viewModel.onEvent(ShoppingListUiEvent.OnEditDismissed)

        assertThat(viewModel.uiState.value.editingItem).isNull()
    }

    @Test
    fun `onErrorDismissed clears error message`() = runTest {
        viewModel.onEvent(ShoppingListUiEvent.OnItemNameChanged(""))
        viewModel.onEvent(ShoppingListUiEvent.OnAddItemClicked)


        viewModel.onEvent(ShoppingListUiEvent.OnErrorDismissed)
        assertThat(viewModel.uiState.value.errorMessage).isNull()
    }
}
