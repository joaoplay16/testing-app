package com.playlab.testingapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.playlab.testingapp.R
import com.playlab.testingapp.data.local.ShoppingItem
import com.playlab.testingapp.launchFragmentInHiltContainer
import com.playlab.testingapp.repositories.FakeShoppingRepositoryAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: TestShoppingFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickInsertIntoDd_shoppingItemInsertedIntoDb(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testViewModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("Shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("2"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("Shopping item", 2, 5.5f, "")
        )
    }

    @Test
    fun clickShoppingItemImageView_navigateToImagePickFragment(){
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> (fragmentFactory = fragmentFactory){
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(click())

        verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
        )
    }

    @Test
    fun pressBackButton_clearCurrentImageUrl() {
        val navController = mock(NavController::class.java)
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        val imageUrl = "TEST"

        launchFragmentInHiltContainer<AddShoppingItemFragment> (fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        testViewModel.setCurImageUrl(imageUrl)

        pressBack()

        val currentImageUrl = testViewModel.curImageUrl.getOrAwaitValue()

        assertThat(currentImageUrl).isEqualTo("")

    }

@Test
fun insertIntoDd_clearInputFields() {
    launchFragmentInHiltContainer<AddShoppingItemFragment>(
        fragmentFactory = fragmentFactory
    )
    onView(withId(R.id.etShoppingItemName)).perform( replaceText("Shopping item"))
    onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("2"))
    onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5"))
    onView(withId(R.id.btnAddShoppingItem)).perform(click())

    onView(withId(R.id.etShoppingItemName)).check(matches(withText("")))
    onView(withId(R.id.etShoppingItemAmount)).check(matches(withText("")))
    onView(withId(R.id.etShoppingItemPrice)).check(matches(withText("")))
}

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> (fragmentFactory = fragmentFactory){
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }
}