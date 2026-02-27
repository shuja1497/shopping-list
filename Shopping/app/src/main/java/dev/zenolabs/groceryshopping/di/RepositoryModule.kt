package dev.zenolabs.groceryshopping.di

import dev.zenolabs.groceryshopping.data.repository.ShoppingRepositoryImpl
import dev.zenolabs.groceryshopping.data.repository.ShoppingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShoppingRepository(impl: ShoppingRepositoryImpl): ShoppingRepository
}
