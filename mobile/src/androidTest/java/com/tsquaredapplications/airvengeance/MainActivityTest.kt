package com.tsquaredapplications.airvengeance

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @Rule @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shouldShowIndoorReadingsFragmentOnLaunch(){
        onView(withId(R.id.pressure_gauge)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowOutdoorReadingsOnOutdoorNavClick(){
        onView(withId(R.id.nav_outdoor_readings)).perform(click())
        onView(withText(activityRule.activity.getString(R.string.outdoor_readings_fragment_title)))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowHistoryOnHistoryNavClick(){
        onView(withId(R.id.nav_history)).perform(click())
        onView(withId(R.id.readings_recycler_view))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowGraphOnGraphNavClick(){
        onView(withId(R.id.nav_graph)).perform(click())
        onView(withId(R.id.graph_view))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowSettingsOnSettingsNavClick(){
        onView(withId(R.id.action_settings)).perform(click())
        onView(withText(activityRule.activity.getString(R.string.temperature_units_label)))
                .check(matches(isDisplayed()))
    }



}
