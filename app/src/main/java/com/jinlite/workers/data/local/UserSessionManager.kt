package com.jinlite.workers.data.local

import android.content.Context
import android.content.SharedPreferences

data class UserSession(
    val workerId: String,
    val cardNo: String,
    val fullNameBn: String,
    val phone: String,
    val plant: String,
    val floor: String,
    val side: String,
    val designation: String,
    val isLoggedIn: Boolean
)

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("jinlite_user_session", Context.MODE_PRIVATE)

    fun saveUser(
        workerId: String,
        cardNo: String,
        fullNameBn: String,
        phone: String,
        plant: String,
        floor: String,
        side: String,
        designation: String
    ) {
        prefs.edit().apply {
            putString("worker_id", workerId)
            putString("card_no", cardNo)
            putString("full_name_bn", fullNameBn)
            putString("phone", phone)
            putString("plant", plant)
            putString("floor", floor)
            putString("side", side)
            putString("designation", designation)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    fun getSession(): UserSession {
        return UserSession(
            workerId = prefs.getString("worker_id", "") ?: "",
            cardNo = prefs.getString("card_no", "") ?: "",
            fullNameBn = prefs.getString("full_name_bn", "") ?: "",
            phone = prefs.getString("phone", "") ?: "",
            plant = prefs.getString("plant", "ALL") ?: "ALL",
            floor = prefs.getString("floor", "ALL") ?: "ALL",
            side = prefs.getString("side", "ALL") ?: "ALL",
            designation = prefs.getString("designation", "অপারেটর") ?: "অপারেটর",
            isLoggedIn = prefs.getBoolean("is_logged_in", false)
        )
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
