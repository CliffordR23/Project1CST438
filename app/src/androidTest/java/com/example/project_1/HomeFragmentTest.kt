package com.example.project_1

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLoadingStateVisibility() {
        // Initial state: ProgressBar should be hidden
        onView(withId(R.id.loading_indicator)).check(matches(not(isDisplayed())))

        // Click submit button
        onView(withId(R.id.button_submit)).perform(click())

        // During loading: ProgressBar should be visible
        onView(withId(R.id.loading_indicator)).check(matches(isDisplayed()))
        
        // During loading: Input fields should be disabled
        onView(withId(R.id.button_submit)).check(matches(not(isEnabled())))
        onView(withId(R.id.edittext_phone_lookup)).check(matches(not(isEnabled())))

        // Wait for mock delay (2 seconds) to finish
        Thread.sleep(2500)

        // After loading: ProgressBar should be hidden again
        onView(withId(R.id.loading_indicator)).check(matches(not(isDisplayed())))
        
        // After loading: Input fields should be re-enabled
        onView(withId(R.id.button_submit)).check(matches(isEnabled()))
        
        // After loading: Text should be updated
        onView(withId(R.id.textview_home)).check(matches(withText("Lookup complete!")))
    }
}