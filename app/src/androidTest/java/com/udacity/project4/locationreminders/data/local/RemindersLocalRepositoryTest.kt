package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    finish to  Add testing implementation to the RemindersLocalRepository.kt at 24/12/2022 -9.20am




    val test1 = ReminderDTO("title1", "description1","location1",latitude=1.0, longitude = 1.0)

    val test2 = ReminderDTO("title2", "description2","location2",latitude=2.0, longitude = 2.0)
    private lateinit var testsLocalDataSource: FakeDataSource

    abstract val remindersDao: RemindersDao
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    // Class under test

    private lateinit var tasksRepository: RemindersLocalRepository

    @Before
    fun createRepository() {

        val  remindersDao: RemindersDao

        tasksRepository = RemindersLocalRepository(

            remindersDao = remindersDao, ioDispatcher = Dispatchers.Unconfined
        )
    }

    //    finish to  Add should shouldturenerror
    fun shouldtReturnError(value: Boolean) {
        shouldReturnError = value
    }
}