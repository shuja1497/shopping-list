package com.droid.shopping.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droid.shopping.data.local.Category
import com.droid.shopping.ui.theme.ShoppingTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryChips(
    selected: Category,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Category.entries.forEach { category ->
            FilterChip(
                selected = category == selected,
                onClick = { onCategorySelected(category) },
                label = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = category.icon, fontSize = 20.sp)
                        Text(text = category.displayName, style = MaterialTheme.typography.labelSmall)
                    }
                },
                modifier = Modifier.size(width = 80.dp, height = 56.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipsPreview() {
    ShoppingTheme {
        CategoryChips(
            selected = Category.FRUITS,
            onCategorySelected = {},
        )
    }
}
