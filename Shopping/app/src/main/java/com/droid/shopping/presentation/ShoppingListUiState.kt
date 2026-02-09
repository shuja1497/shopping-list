package com.droid.shopping.presentation

import androidx.compose.runtime.Immutable
import com.droid.shopping.data.local.Category
import com.droid.shopping.data.local.ShoppingItem

@Immutable
data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val itemName: String = "",
    val selectedCategory: Category = Category.MILK,
    val filterCategory: Category? = null,
    val sortOption: SortOption = SortOption.DEFAULT,
    val editingItem: ShoppingItem? = null,
    val editName: String = "",
    val editCategory: Category = Category.MILK,
    val errorMessage: String? = null,
) {
    val filteredItems: List<ShoppingItem>
        get() {
            val filtered = if (filterCategory != null) {
                items.filter { it.category == filterCategory }
            } else {
                items
            }
            return when (sortOption) {
                SortOption.DEFAULT -> filtered.sortedWith(
                    compareBy<ShoppingItem> { it.isPurchased }.thenByDescending { it.id }
                )
                SortOption.ALPHABETICAL -> filtered.sortedBy { it.name.lowercase() }
                SortOption.CATEGORY -> filtered.sortedBy { it.category.name }
                SortOption.STATUS -> filtered.sortedBy { it.isPurchased }
            }
        }
}

enum class SortOption(val displayName: String) {
    DEFAULT("Default"),
    ALPHABETICAL("A-Z"),
    CATEGORY("Category"),
    STATUS("Status"),
}
