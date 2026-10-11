package com.jinlite.workers

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinlite.workers.data.local.UserSessionManager
import com.jinlite.workers.ui.screens.*
import com.jinlite.workers.ui.theme.JinliteWorkersTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as JinliteApp
        val repository = app.repository
        val sessionManager = UserSessionManager(this)

        setContent {
            JinliteWorkersTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                var currentSession by remember { mutableStateOf(sessionManager.getSession()) }
                var isAuthLoading by remember { mutableStateOf(false) }

                // রুম অফলাইন ডেটাবেস থেকে লাইভ Flow সংগ্রহ
                val notices by repository.getNoticesFlow(
                    plant = if (currentSession.isLoggedIn) currentSession.plant else "ALL",
                    floor = if (currentSession.isLoggedIn) currentSession.floor else "ALL",
                    side = if (currentSession.isLoggedIn) currentSession.side else "ALL"
                ).collectAsState(initial = emptyList())

                val holidays by repository.getHolidaysFlow().collectAsState(initial = emptyList())
                val rules by repository.getRulesFlow().collectAsState(initial = emptyList())
                val myGrievances by repository.getMyGrievancesFlow().collectAsState(initial = emptyList())

                var isOffline by remember { mutableStateOf(!repository.isNetworkAvailable()) }

                // রিফ্রেশ ফাংশন
                val onRefresh = {
                    coroutineScope.launch {
                        isOffline = !repository.isNetworkAvailable()
                        if (isOffline) {
                            Toast.makeText(this@MainActivity, "ইন্টারনেট নেই। সংরক্ষিত অফলাইন ডেটা দেখানো হচ্ছে।", Toast.LENGTH_SHORT).show()
                        } else {
                            val success = repository.syncAllData(
                                plant = currentSession.plant,
                                floor = currentSession.floor,
                                side = currentSession.side
                            )
                            if (success) {
                                Toast.makeText(this@MainActivity, "সকল তথ্য সফলভাবে সিঙ্ক হয়েছে!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity, "সিঙ্ক ব্যর্থ হয়েছে।", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    val startDestination = if (currentSession.isLoggedIn) "home" else "login"

                    NavHost(navController = navController, startDestination = startDestination) {

                        // --- ১. লগইন এবং রেজিস্ট্রেশন স্ক্রিন ---
                        composable("login") {
                            LoginScreen(
                                isLoading = isAuthLoading,
                                onLogin = { cardNo, phone ->
                                    coroutineScope.launch {
                                        isAuthLoading = true
                                        val res = repository.loginWorker(cardNo, phone)
                                        isAuthLoading = false
                                        res.onSuccess { data ->
                                            val wId = data["worker_id"]?.toString() ?: "JLW-00001"
                                            val name = data["full_name_bn"]?.toString() ?: "কর্মী"
                                            val plant = data["plant"]?.toString() ?: "ALL"
                                            val floor = data["floor"]?.toString() ?: "ALL"
                                            val side = data["side"]?.toString() ?: "ALL"
                                            val desig = data["designation"]?.toString() ?: "অপারেটর"

                                            sessionManager.saveUser(wId, cardNo, name, phone, plant, floor, side, desig)
                                            currentSession = sessionManager.getSession()

                                            Toast.makeText(this@MainActivity, "স্বাগতম, $name!", Toast.LENGTH_SHORT).show()
                                            onRefresh()
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }.onFailure { err ->
                                            Toast.makeText(this@MainActivity, err.message ?: "লগইন ব্যর্থ হয়েছে", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                },
                                onRegister = { cardNo, phone, name, plant, floor, side, desig ->
                                    coroutineScope.launch {
                                        isAuthLoading = true
                                        val res = repository.registerWorker(cardNo, phone, name, plant, floor, side, desig)
                                        isAuthLoading = false
                                        res.onSuccess { data ->
                                            val wId = data["worker_id"]?.toString() ?: "JLW-NEW"
                                            sessionManager.saveUser(wId, cardNo, name, phone, plant, floor, side, desig)
                                            currentSession = sessionManager.getSession()

                                            Toast.makeText(this@MainActivity, "নিবন্ধন সম্পন্ন হয়েছে! আইডি: $wId", Toast.LENGTH_LONG).show()
                                            onRefresh()
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }.onFailure { err ->
                                            Toast.makeText(this@MainActivity, err.message ?: "নিবন্ধন ব্যর্থ হয়েছে", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            )
                        }

                        // --- ২. হোম স্ক্রিন ---
                        composable("home") {
                            HomeScreen(
                                notices = notices,
                                isOffline = isOffline,
                                onRefresh = { onRefresh() },
                                onNavigateToNotices = { navController.navigate("notices") },
                                onNavigateToGrievances = { navController.navigate("grievances") },
                                onNavigateToHolidays = { navController.navigate("holidays") },
                                onNavigateToQuiz = { navController.navigate("quiz") },
                                onNoticeClick = { navController.navigate("notices") }
                            )
                        }

                        // --- ৩. নোটিশ বোর্ড স্ক্রিন ---
                        composable("notices") {
                            NoticeBoardScreen(
                                notices = notices,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // --- ৪. অভিযোগ স্ক্রিন ---
                        composable("grievances") {
                            GrievanceScreen(
                                savedGrievances = myGrievances,
                                onBack = { navController.popBackStack() },
                                onSubmit = { isAnon, plant, floor, side, cat, sub, det ->
                                    coroutineScope.launch {
                                        val result = repository.submitGrievance(
                                            workerId = if (currentSession.isLoggedIn) currentSession.workerId else "JLW-00001",
                                            isAnonymous = isAnon,
                                            plant = plant,
                                            floor = floor,
                                            side = side,
                                            category = cat,
                                            subject = sub,
                                            details = det
                                        )
                                        result.onSuccess { code ->
                                            Toast.makeText(this@MainActivity, "অভিযোগ সফলভাবে জমা হয়েছে! ট্র্যাকিং কোড: $code", Toast.LENGTH_LONG).show()
                                        }.onFailure { err ->
                                            Toast.makeText(this@MainActivity, err.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }

                        // --- ৫. ছুটির তালিকা ও নিয়মাবলী ---
                        composable("holidays") {
                            HolidaysRulesScreen(
                                holidays = holidays,
                                rules = rules,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // --- ৬. কুইজ ও লিডারবোর্ড ---
                        composable("quiz") {
                            QuizLeaderboardScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
