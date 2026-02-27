package dev.zenolabs.groceryshopping.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zenolabs.groceryshopping.data.local.Category
import dev.zenolabs.groceryshopping.ui.theme.ShoppingTheme

@Composable
fun AddItemSection(
    itemName: String,
    selectedCategory: Category,
    onNameChanged: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onAddClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Add New Item",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = itemName,
                onValueChange = onNameChanged,
                label = { Text("Item Name") },
                placeholder = { Text("Enter grocery item...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "Category",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            CategoryChips(
                selected = selectedCategory,
                onCategorySelected = onCategorySelected,
            )

            Button(
                onClick = onAddClicked,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                ),
            ) {
                Text("+ Add Item")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddItemSectionPreview() {
    ShoppingTheme {
        AddItemSection(
            itemName = "Bananas",
            selectedCategory = Category.FRUITS,
            onNameChanged = {},
            onCategorySelected = {},
            onAddClicked = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
