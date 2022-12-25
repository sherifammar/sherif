package com.udacity.project4.locationreminders.reminderslist

import android.R
import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.tasks.Task
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import net.bytebuddy.matcher.ElementMatchers.`is`
import net.bytebuddy.matcher.ElementMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.util.*
import java.util.regex.Pattern.matches


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

//    finish totest the navigation of the fragments 24/12/2022 -12.50pm.
//     test the displayed data on the UI.
//     add testing for the error messages.


    private lateinit var repository: RemindersLocalRepository
    // GIVEN - Add completed task to the DB
    @Test
    fun completedTaskDetails_DisplayedInUi() = runBlockingTest{
        // GIVEN - Add completed task to the DB
        val completedTask = Task("Completed Task", "AndroidX Rocks", true)
        repository. getReminder(completedTask)
        val bundle = ReminderListFragmentArgs(completedTask.id).toBundle()
        launchFragmentInContainer<ReminderListFragment>(bundle, R.style.AppTheme)

        // finish to buttom of selection
        onView(withId(R.id.savelocation)).perform(click())

// finish to check buttom
        onView(withId(R.id.addReminderFAB)).perform(click())
// finish to check text view
        onView(withId(R.id.noDataTextView))
// finish to check progress bar
        onView(withId(R.id.progressBar))
// finish to test recyel view
        @Test(expected = PerformException::class)
        fun itemWithText_doesNotExist() {
            // Attempt to scroll to an item that contains the special text.
            onView(ViewMatchers.withId(R.id.reminderssRecyclerView))
                .perform(
                    // scrollTo will fail the test if no item matches.
                    RecyclerViewActions.scrollTo(
                        hasDescendant(withText("not in the list"))


                    )

                )
        }


        onView(withId(R.id.reminderssRecyclerView))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("TITLE1")), click()))

// finish to using mokito
        @Test
        fun clickfloatingbutton() {

            val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            val navController = mock(NavController::class.java)
            scenario.onFragment {
                Navigation.setViewNavController(it.view!!, navController)
            }}


// finish to test toast
        onView(withText("reminder save")).inRoot(
            withDecorView(
                not(
                    `is`(
                        getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()))
//finish to test of snabar
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.whatever_is_your_text)))
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


    @Test
    fun clickTask_navigateFragment() = runBlockingTest {

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), com.udacity.project4.R.style.AppTheme)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the first list item
        onView(withId(com.udacity.project4.R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("TITLE1")), click()))




    }
// tet of errors
    @Test
    fun testShouldShowErrorViewOnNetworkError() {

        onView(withId(R.id.geofence_unknown_error)).check(matches(isDisplayed()))
        onView(withId(R.id.geofence_not_available)).check(matches(isDisplayed()))
        onView(withId(R.id.geofence_too_many_geofences)).check(matches(isDisplayed()))
        onView(withId(R.id.geofence_too_many_pending_intents)).check(matches(isDisplayed()))
        onView(withId(R.id.error_adding_geofence)).check(matches(isDisplayed()))
    }


}
