package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    finish to  Add testing implementation to the RemindersDao.kt
    // using  Room.inMemoryDatabaseBuilder to create a Room DB instance at 24-12-2022/ 9 am .




    private lateinit var database: ReminderDataSource
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ReminderDataSource::class.java
        ).build()

        @After
        fun closeDb() = database.close()

    }

    @Test
    fun insertTest() = runBlockingTest {
        // GIVEN - Insert a task.
        val test =ReminderDTO("title", "description","location",latitude=null, longitude = null)
        database.RemindersDao.saveReminder(test)
        



    }


}