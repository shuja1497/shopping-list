package dev.zenolabs.groceryshopping.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zenolabs.groceryshopping.data.local.ShoppingItem
import dev.zenolabs.groceryshopping.data.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ShoppingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    init {
        observeItems()
    }

    private fun observeItems() {
        repository.getAllItems()
            .onEach { items ->
                _uiState.update { it.copy(items = items) }
            }
            .catch { _ ->
                _uiState.update { it.copy(errorMessage = "Failed to load items") }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ShoppingListUiEvent) {
        when (event) {
            is ShoppingListUiEvent.OnItemNameChanged -> {
                _uiState.update { it.copy(itemName = event.name) }
            }

            is ShoppingListUiEvent.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
            }

            is ShoppingListUiEvent.OnAddItemClicked -> {
                addItem()
            }

            is ShoppingListUiEvent.OnTogglePurchased -> {
                togglePurchased(event.item)
            }

            is ShoppingListUiEvent.OnDeleteItem -> {
                deleteItem(event.item)
            }

            is ShoppingListUiEvent.OnFilterChanged -> {
                _uiState.update { it.copy(filterCategory = event.category) }
            }

            is ShoppingListUiEvent.OnSortChanged -> {
                _uiState.update { it.copy(sortOption = event.sortOption) }
            }

            is ShoppingListUiEvent.OnEditItem -> {
                _uiState.update {
                    it.copy(
                        editingItem = event.item,
                        editName = event.item.name,
                        editCategory = event.item.category,
                    )
                }
            }

            is ShoppingListUiEvent.OnEditNameChanged -> {
                _uiState.update { it.copy(editName = event.name) }
            }

            is ShoppingListUiEvent.OnEditCategoryChanged -> {
                _uiState.update { it.copy(editCategory = event.category) }
            }

            is ShoppingListUiEvent.OnEditConfirmed -> {
                confirmEdit()
            }

            is ShoppingListUiEvent.OnEditDismissed -> {
                _uiState.update { it.copy(editingItem = null) }
            }

            is ShoppingListUiEvent.OnErrorDismissed -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun addItem() {
        val state = _uiState.value
        val name = state.itemName.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Item name cannot be empty") }
            return
        }
        viewModelScope.launch {
            try {
                repository.addItem(
                    ShoppingItem(
                        name = name,
                        category = state.selectedCategory,
                    )
                )
                _uiState.update { it.copy(itemName = "") }
            } catch (_: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to add item") }
            }
        }
    }

    private fun togglePurchased(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.updateItem(item.copy(isPurchased = !item.isPurchased))
            } catch (_: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to update item") }
            }
        }
    }

    private fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.deleteItem(item)
            } catch (_: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to delete item") }
            }
        }
    }

    private fun confirmEdit() {
        val state = _uiState.value
        val editingItem = state.editingItem ?: return
        val name = state.editName.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Item name cannot be empty") }
            return
        }
        viewModelScope.launch {
            try {
                repository.updateItem(
                    editingItem.copy(
                        name = name,
                        category = state.editCategory,
                    )
                )
                _uiState.update { it.copy(editingItem = null) }
            } catch (_: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to edit item") }
            }
        }
    }
}
