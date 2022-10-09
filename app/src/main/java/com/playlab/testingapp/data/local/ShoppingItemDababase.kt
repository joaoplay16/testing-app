package com.playlab.testingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingItemDababase : RoomDatabase(){

    abstract fun shoppingDao(): ShoppingDao
}