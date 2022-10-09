package com.playlab.testingapp.di

import android.content.Context
import androidx.room.Room
import com.playlab.testingapp.data.local.ShoppingItemDababase
import com.playlab.testingapp.data.remote.PixabayAPI
import com.playlab.testingapp.other.Other.BASE_URL
import com.playlab.testingapp.other.Other.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ShoppingItemDababase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDababase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }

}