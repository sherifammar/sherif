package com.udacity.project4

import android.app.Application
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import java.util.regex.Pattern

@RunWith(AndroidJUnit4::class)
@LargeTest
//END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {// Extended Koin Test - embed autoclose @after method to close Koin after every test

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }


//  finish to add End to End testing to the app

    @Test
    fun editTask() = runBlocking {
        // Set initial state.
        repository.saveReminder( ReminderDTO("title1", "description1","location1",latitude=1.0, longitude = 1.0)
        )


        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)

        // finish to buttom of selection
        Espresso.onView(withId(R.id.savelocation)).perform(ViewActions.click())

// finish to check buttom
        Espresso.onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())
// finish to check text view
        Espresso.onView(withId(R.id.noDataTextView))
// finish to check progress bar
        Espresso.onView(withId(R.id.progressBar))
        Espresso.onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("TITLE1")), ViewActions.click()
                ))


       // finish to check buttom
                Espresso.onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())
// finish to check text view
        Espresso.onView(withId(R.id.selectLocation))

        //edittext
        Espresso.onView(withId(R.id.reminderTitle))
            .perform(ViewActions.clearText(), ViewActions.typeText(""))
        Espresso.onView(withId(R.id.reminderDescription))
            .perform(ViewActions.clearText(), ViewActions.typeText(""))

        Espresso.onView(withId(R.id.auth_button)).perform(ViewActions.click())


                    activityScenario . close ()
    }


}
