package com.droid.shopping.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droid.shopping.data.local.Category
import com.droid.shopping.data.local.ShoppingItem
import com.droid.shopping.presentation.components.AddItemSection
import com.droid.shopping.presentation.components.EditItemDialog
import com.droid.shopping.presentation.components.EmptyState
import com.droid.shopping.presentation.components.FilterBar
import com.droid.shopping.presentation.components.ShoppingItemCard
import com.droid.shopping.ui.theme.ShoppingTheme

@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ShoppingListContent(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShoppingListContent(
    state: ShoppingListUiState,
    onEvent: (ShoppingListUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            onEvent(ShoppingListUiEvent.OnErrorDismissed)
        }
    }

    if (state.editingItem != null) {
        EditItemDialog(
            name = state.editName,
            category = state.editCategory,
            onNameChanged = {
                onEvent(ShoppingListUiEvent.OnEditNameChanged(it))
            },
            onCategoryChanged = {
                onEvent(ShoppingListUiEvent.OnEditCategoryChanged(it))
            },
            onConfirm = {
                onEvent(ShoppingListUiEvent.OnEditConfirmed)
            },
            onDismiss = {
                onEvent(ShoppingListUiEvent.OnEditDismissed)
            },
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Grocery List",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(28.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "add_section") {
                AddItemSection(
                    itemName = state.itemName,
                    selectedCategory = state.selectedCategory,
                    onNameChanged = {
                        onEvent(ShoppingListUiEvent.OnItemNameChanged(it))
                    },
                    onCategorySelected = {
                        onEvent(ShoppingListUiEvent.OnCategorySelected(it))
                    },
                    onAddClicked = {
                        onEvent(ShoppingListUiEvent.OnAddItemClicked)
                    },
                )
            }

            item(key = "filter_bar") {
                FilterBar(
                    selectedFilter = state.filterCategory,
                    selectedSort = state.sortOption,
                    onFilterChanged = {
                        onEvent(ShoppingListUiEvent.OnFilterChanged(it))
                    },
                    onSortChanged = {
                        onEvent(ShoppingListUiEvent.OnSortChanged(it))
                    },
                )
            }

            if (state.filteredItems.isEmpty()) {
                item(key = "empty") {
                    EmptyState(modifier = Modifier.fillMaxWidth())
                }
            } else {
                items(
                    items = state.filteredItems,
                    key = { it.id },
                ) { item ->
                    ShoppingItemCard(
                        item = item,
                        onTogglePurchased = {
                            onEvent(ShoppingListUiEvent.OnTogglePurchased(item))
                        },
                        onEdit = {
                            onEvent(ShoppingListUiEvent.OnEditItem(item))
                        },
                        onDelete = {
                            onEvent(ShoppingListUiEvent.OnDeleteItem(item))
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ShoppingListScreenEmptyPreview() {
    ShoppingTheme {
        ShoppingListContent(
            state = ShoppingListUiState(),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ShoppingListScreenWithItemsPreview() {
    ShoppingTheme {
        ShoppingListContent(
            state = ShoppingListUiState(
                items = listOf(
                    ShoppingItem(id = 1, name = "Whole Milk", category = Category.MILK),
                    ShoppingItem(id = 2, name = "Bananas", category = Category.FRUITS),
                    ShoppingItem(
                        id = 3,
                        name = "Chicken Breast",
                        category = Category.MEATS,
                        isPurchased = true
                    ),
                    ShoppingItem(id = 4, name = "Spinach", category = Category.VEGETABLES),
                    ShoppingItem(id = 5, name = "Bread", category = Category.BREADS),
                ),
            ),
            onEvent = {},
        )
    }
}
