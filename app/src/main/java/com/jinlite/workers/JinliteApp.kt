package com.jinlite.workers

import android.app.Application
import com.jinlite.workers.data.local.AppDatabase
import com.jinlite.workers.data.repository.WorkerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class JinliteApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { WorkerRepository(this, database) }

    override fun onCreate() {
        super.onCreate()

        // অ্যাপ ওপেন হওয়ার সাথে সাথে ব্যাকগ্রাউন্ডে স্বয়ংক্রিয় অফলাইন সিঙ্ক
        applicationScope.launch {
            if (repository.isNetworkAvailable()) {
                repository.syncAllData()
            }
        }
    }
}
