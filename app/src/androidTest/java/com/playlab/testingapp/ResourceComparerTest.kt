package com.playlab.testingapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ResourceComparerTest {

    private lateinit var resourceComparer: ResourceComparer

    @Before
    fun setup(){
        resourceComparer = ResourceComparer()
    }

    @After
    fun teardown(){

    }

    @Test
    fun stringResourceSameAsGivenString_returnsTrue(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.app_name, "TestingApp")
        assertThat(result).isTrue()
    }

    @Test
    fun stringResourceSameAsGivenString_returnsFalse(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceComparer.isEqual(context, R.string.app_name, "TestingApp2")
        assertThat(result).isFalse()
    }

}