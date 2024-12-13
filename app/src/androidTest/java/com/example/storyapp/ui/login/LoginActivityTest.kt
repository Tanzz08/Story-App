package com.example.storyapp.ui.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.storyapp.R
import com.example.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {

        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginAndLogout() {

        onView(withId(R.id.ed_login_email)).perform(typeText("bustanuldarking23@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(typeText("bustanul"), closeSoftKeyboard())

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.rvStories)).check(matches(isDisplayed()))

        activityRule.scenario.onActivity { activity ->
            Espresso.openActionBarOverflowOrOptionsMenu(activity) // Buka Overflow Menu di dalam onActivity()
        }

        onView(withText(R.string.logout)).perform(click()) // Klik pada item "Logout" di Overflow Menu

        onView(withId(R.id.action_logout)).perform(click())

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }
}
