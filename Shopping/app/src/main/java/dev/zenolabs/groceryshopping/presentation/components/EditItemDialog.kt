package dev.zenolabs.groceryshopping.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zenolabs.groceryshopping.data.local.Category
import dev.zenolabs.groceryshopping.ui.theme.ShoppingTheme

@Composable
fun EditItemDialog(
    name: String,
    category: Category,
    onNameChanged: (String) -> Unit,
    onCategoryChanged: (Category) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChanged,
                    label = { Text("Item Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                CategoryChips(
                    selected = category,
                    onCategorySelected = onCategoryChanged,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

@Preview
@Composable
private fun EditItemDialogPreview() {
    ShoppingTheme {
        EditItemDialog(
            name = "Bananas",
            category = Category.FRUITS,
            onNameChanged = {},
            onCategoryChanged = {},
            onConfirm = {},
            onDismiss = {},
        )
    }
}
