package com.udacity.project4.locationreminders.reminderslist

import android.service.autofill.Validators
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.ExpectFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.bytebuddy.implementation.FixedValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //finish to  provide testing to the RemindersListViewModel and its live data objects 24/12/2022 at 1.45pm


    private lateinit var reminderViewModel: RemindersListViewModel


    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        reminderViewModel= RemindersListViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun loadReminders_show() {

        // When adding a new task

        reminderViewModel.loadReminders()

        // Then the new task event is triggered
        val value = reminderViewModel. dataList.addAll.awaitNextValue()
        ExpectFailure.assertThat(
            value?.getContentIfNotHandled(), (Validators.not(FixedValue.nullValue()))
        )
    }
}