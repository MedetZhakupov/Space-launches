package com.medetzhakupov.robot

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.medetzhakupov.R
import com.medetzhakupov.ui.main.SpaceLaunchesAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher


class MainFragmentRobot {

    fun checkSpaceLaunchesAreDisplayed() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    fun clickOnFirstSpaceLaunch() {
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition<SpaceLaunchesAdapter.VH>(0, click()))
    }

    fun typeOnSearch(search: String) {
        onView(withId(R.id.search_edit_text)).perform(typeText(search))
    }

    fun checkSearchedItemIsDisplayed(search: String) {
//        onView(withId(R.id.recycler_view))
//            .check(matches(atPosition(0, hasDescendant(withText(search)))))

        onView(withText(search)).check(matches(isDisplayed()))
    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}



fun mainFragment(block: MainFragmentRobot.() -> Unit) = MainFragmentRobot().apply(block)