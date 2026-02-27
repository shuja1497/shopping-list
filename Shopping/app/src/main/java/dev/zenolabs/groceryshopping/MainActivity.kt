package dev.zenolabs.groceryshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dev.zenolabs.groceryshopping.presentation.ShoppingListScreen
import dev.zenolabs.groceryshopping.ui.theme.ShoppingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                ShoppingListScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}