package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.tasks.Task
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.*
import java.util.regex.Pattern.matches

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest


class Savefragmenttest {
    private lateinit var repository: RemindersLocalRepository
    @Test
    fun saveReminderFragment() {

        val scenario = launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }


        Espresso.onView(ViewMatchers.withId(R.id.saveReminder)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            SaveReminderFragmentDirection.action_saveReminderFragment_to_reminderListFragment(
                null, ApplicationProvider.getApplicationContext<Context>()

            )
        )


        Mockito.verify(navController).navigate(
            SaveReminderFragmentDirection.action_saveReminderFragment_to_SelectLocationFragment(
                null, ApplicationProvider.getApplicationContext<Context>()

            )
        )
    }

    @Test
    fun completedTaskDetails_DisplayedInUi() = runBlockingTest{
        // GIVEN - Add completed task to the DB
        val completedTask = Task("Completed Task", "AndroidX Rocks", true)
        repository. saveReminder(completedTask)

// finish to check buttom
    onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.saveReminder)).perform(click())
// finish to check text view
    onView(withId(R.id.selectLocation))

        //edittext
        onView(withId(R.id.reminderTitle)).perform(clearText(), typeText(""))
        onView(withId(R.id.reminderDescription)).perform(clearText(), typeText(""))

        onView(withId(R.id.selectLocation)).check(matches(withText("ReminderLocation")))




    }







}
@Before
fun initRepository() {
    repository = FakeAndroidTestRepository()
    ServiceLoader.ReminderLocalReposity = repository
}

@After
fun cleanupDb() = runBlockingTest {
    ServiceLoader.resetRepository()
}