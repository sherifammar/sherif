package com.udacity.project4.locationreminders.data

import androidx.lifecycle.MutableLiveData
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlin.math.tanh

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var tasks :MutableLiveData<ReminderDTO>?= mutableListOf()) : ReminderDataSource {

//    finish to  Create a fake data source to act as a double to the real data source at 24/12/2022 at 1.40pm

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        // finish to  ("Return the reminders")
        tasks?.let { return Success(ArrayList(it)) } return Error(Exception("Tasks not found"))
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        // finish to ("save the reminder")
        tasks?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        // finish to ("return the reminder with the id")
        tasks?.let { return Success(ArrayList(it)) }return Error(Exception("Tasks not found"))
    }

    override suspend fun deleteAllReminders() {
        // finish to ("delete all the reminders")
        tasks?.clear()
    }




}