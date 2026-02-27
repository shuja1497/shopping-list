package dev.zenolabs.groceryshopping.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zenolabs.groceryshopping.data.local.Category
import dev.zenolabs.groceryshopping.presentation.SortOption
import dev.zenolabs.groceryshopping.ui.theme.ShoppingTheme

@Composable
fun FilterBar(
    selectedFilter: Category?,
    selectedSort: SortOption,
    onFilterChanged: (Category?) -> Unit,
    onSortChanged: (SortOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "Filter by Category",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onFilterChanged(null) },
                label = { Text("All") },
            )
            Category.entries.forEach { category ->
                FilterChip(
                    selected = selectedFilter == category,
                    onClick = {
                        onFilterChanged(if (selectedFilter == category) null else category)
                    },
                    label = { Text("${category.icon} ${category.displayName}") },
                )
            }
        }

        Text(
            text = "Sort by",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SortOption.entries.forEach { option ->
                FilterChip(
                    selected = selectedSort == option,
                    onClick = { onSortChanged(option) },
                    label = { Text(option.displayName) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBarPreview() {
    ShoppingTheme {
        FilterBar(
            selectedFilter = null,
            selectedSort = SortOption.DEFAULT,
            onFilterChanged = {},
            onSortChanged = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
