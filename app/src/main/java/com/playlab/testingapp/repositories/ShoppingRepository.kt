package com.playlab.testingapp.repositories

import androidx.lifecycle.LiveData
import com.playlab.testingapp.data.local.ShoppingItem
import com.playlab.testingapp.data.remote.responses.ImageResponse
import com.playlab.testingapp.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}