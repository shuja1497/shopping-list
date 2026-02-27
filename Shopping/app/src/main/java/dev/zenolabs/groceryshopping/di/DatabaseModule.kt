package dev.zenolabs.groceryshopping.di

import android.content.Context
import androidx.room.Room
import dev.zenolabs.groceryshopping.data.local.DatabasePassphraseManager
import dev.zenolabs.groceryshopping.data.local.ShoppingDatabase
import dev.zenolabs.groceryshopping.data.local.ShoppingItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShoppingDatabase {
        val passphrase = DatabasePassphraseManager.getPassphrase(context)
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            "shopping_db",
        )
            .openHelperFactory(factory)
            // Add explicit Migration objects here when bumping the DB version.
            // Example: .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            // fallbackToDestructiveMigration is a safety net: if a migration
            // path is missing, the DB is wiped rather than crashing the app.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideShoppingItemDao(database: ShoppingDatabase): ShoppingItemDao =
        database.shoppingItemDao()
}
