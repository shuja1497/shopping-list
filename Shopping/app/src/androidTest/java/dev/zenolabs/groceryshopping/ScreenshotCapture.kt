package dev.zenolabs.groceryshopping

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import dev.zenolabs.groceryshopping.data.local.Category
import dev.zenolabs.groceryshopping.data.local.ShoppingItem
import dev.zenolabs.groceryshopping.presentation.ShoppingListContent
import dev.zenolabs.groceryshopping.presentation.ShoppingListUiState
import dev.zenolabs.groceryshopping.presentation.SortOption
import dev.zenolabs.groceryshopping.ui.theme.ShoppingTheme
import org.junit.Rule
import org.junit.Test
import java.io.File

class ScreenshotCapture {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleItems = listOf(
        ShoppingItem(id = 1, name = "Whole Milk", category = Category.MILK),
        ShoppingItem(id = 2, name = "Spinach", category = Category.VEGETABLES),
        ShoppingItem(id = 3, name = "Bananas", category = Category.FRUITS, isPurchased = true),
        ShoppingItem(id = 4, name = "Sourdough Bread", category = Category.BREADS),
        ShoppingItem(id = 5, name = "Chicken Breast", category = Category.MEATS),
        ShoppingItem(id = 6, name = "Apples", category = Category.FRUITS),
        ShoppingItem(id = 7, name = "Carrots", category = Category.VEGETABLES, isPurchased = true),
    )

    private fun saveScreenshot(name: String) {
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dir = File(context.filesDir, "store-screenshots")
        dir.mkdirs()
        val file = File(dir, "$name.png")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test
    fun screenshot01_mainList() {
        composeTestRule.setContent {
            ShoppingTheme {
                ShoppingListContent(
                    state = ShoppingListUiState(items = sampleItems),
                    onEvent = {},
                )
            }
        }
        composeTestRule.waitForIdle()
        saveScreenshot("01_main_list")
    }

    @Test
    fun screenshot02_addItem() {
        composeTestRule.setContent {
            ShoppingTheme {
                ShoppingListContent(
                    state = ShoppingListUiState(
                        items = sampleItems,
                        itemName = "Greek Yogurt",
                        selectedCategory = Category.MILK,
                    ),
                    onEvent = {},
                )
            }
        }
        composeTestRule.waitForIdle()
        saveScreenshot("02_add_item")
    }

    @Test
    fun screenshot03_filterCategory() {
        composeTestRule.setContent {
            ShoppingTheme {
                ShoppingListContent(
                    state = ShoppingListUiState(
                        items = sampleItems,
                        filterCategory = Category.FRUITS,
                    ),
                    onEvent = {},
                )
            }
        }
        composeTestRule.waitForIdle()
        saveScreenshot("03_filter_category")
    }

    @Test
    fun screenshot04_sortAlphabetical() {
        composeTestRule.setContent {
            ShoppingTheme {
                ShoppingListContent(
                    state = ShoppingListUiState(
                        items = sampleItems,
                        sortOption = SortOption.ALPHABETICAL,
                    ),
                    onEvent = {},
                )
            }
        }
        composeTestRule.waitForIdle()
        saveScreenshot("04_sort_alphabetical")
    }

    @Test
    fun screenshot05_emptyState() {
        composeTestRule.setContent {
            ShoppingTheme {
                ShoppingListContent(
                    state = ShoppingListUiState(items = emptyList()),
                    onEvent = {},
                )
            }
        }
        composeTestRule.waitForIdle()
        saveScreenshot("05_empty_state")
    }
}
