package com.playlab.testingapp.di

import android.content.Context
import androidx.room.Room
import com.playlab.testingapp.data.local.ShoppingItemDababase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context): ShoppingItemDababase =
        Room.inMemoryDatabaseBuilder(context, ShoppingItemDababase::class.java)
            .allowMainThreadQueries()
            .build()
}
