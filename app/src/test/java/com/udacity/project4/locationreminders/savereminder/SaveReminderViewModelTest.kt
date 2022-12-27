package com.udacity.project4.locationreminders.savereminder

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

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    //finish to provide testing to the SaveReminderView and its live data objects 24/12/2022 at 2pm

    private lateinit var saveViewModel: SaveReminderViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        saveViewModel= SaveReminderViewModel(ApplicationProvider.getApplicationContext())
    }


    @Test
    fun saveit() {

        saveViewModel.saveReminder()


        val value = saveViewModel. dataSource.saveReminder.awaitNextValue()
        ExpectFailure.assertThat(
            value?.getContentIfNotHandled(), (Validators.not(FixedValue.nullValue()))
        )
    }


}