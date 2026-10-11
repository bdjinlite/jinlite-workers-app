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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jinlite.workers.ui.screens.*
import com.jinlite.workers.ui.theme.JinliteWorkersTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as JinliteApp
        val repository = app.repository

        setContent {
            JinliteWorkersTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                // রুম অফলাইন ডেটাবেস থেকে লাইভ Flow সংগ্রহ
                val notices by repository.getNoticesFlow().collectAsState(initial = emptyList())
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
                            val success = repository.syncAllData()
                            if (success) {
                                Toast.makeText(this@MainActivity, "সকল তথ্য সফলভাবে সিঙ্ক হয়েছে!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity, "সিঙ্ক ব্যর্থ হয়েছে।", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "home") {
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

                        composable("notices") {
                            NoticeBoardScreen(
                                notices = notices,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("grievances") {
                            GrievanceScreen(
                                savedGrievances = myGrievances,
                                onBack = { navController.popBackStack() },
                                onSubmit = { isAnon, plant, floor, side, cat, sub, det ->
                                    coroutineScope.launch {
                                        val result = repository.submitGrievance(
                                            workerId = "JLW-00001",
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

                        composable("holidays") {
                            HolidaysRulesScreen(
                                holidays = holidays,
                                rules = rules,
                                onBack = { navController.popBackStack() }
                            )
                        }

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
