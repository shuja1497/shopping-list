package dev.zenolabs.groceryshopping.presentation

import dev.zenolabs.groceryshopping.data.local.Category
import dev.zenolabs.groceryshopping.data.local.ShoppingItem

sealed interface ShoppingListUiEvent {
    data class OnItemNameChanged(val name: String) : ShoppingListUiEvent

    data class OnCategorySelected(val category: Category) : ShoppingListUiEvent

    data object OnAddItemClicked : ShoppingListUiEvent

    data class OnTogglePurchased(val item: ShoppingItem) : ShoppingListUiEvent

    data class OnDeleteItem(val item: ShoppingItem) : ShoppingListUiEvent

    data class OnFilterChanged(val category: Category?) : ShoppingListUiEvent

    data class OnSortChanged(val sortOption: SortOption) : ShoppingListUiEvent

    data class OnEditItem(val item: ShoppingItem) : ShoppingListUiEvent

    data class OnEditNameChanged(val name: String) : ShoppingListUiEvent

    data class OnEditCategoryChanged(val category: Category) : ShoppingListUiEvent

    data object OnEditConfirmed : ShoppingListUiEvent

    data object OnEditDismissed : ShoppingListUiEvent

    data object OnErrorDismissed : ShoppingListUiEvent
}
