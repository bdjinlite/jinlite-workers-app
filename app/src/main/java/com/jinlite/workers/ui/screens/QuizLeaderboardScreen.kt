package com.jinlite.workers.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinlite.workers.ui.theme.AmberAccent
import com.jinlite.workers.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizLeaderboardScreen(
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // ডেমো লিডারবোর্ড আইটেম
    val demoLeaderboard = listOf(
        Triple(1, "মোঃ রফিকুল ***", 250),
        Triple(2, "মোসাঃ ফাতেমা ***", 210),
        Triple(3, "মোঃ শামীম ***", 180),
        Triple(4, "আব্দুল কাদ্দুস ***", 150),
        Triple(5, "রোকেয়া বেগম ***", 130)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("কুইজ ও লিডারবোর্ড", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("দৈনিক কুইজ", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("মাসিক লিডারবোর্ড", fontWeight = FontWeight.Bold) }
                )
            }

            if (selectedTab == 0) {
                // কুইজ সেকশন
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("আজকের সুরক্ষা কুইজ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NavyPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("১. কারখানায় ফায়ার অ্যালার্ম বাজলে প্রথম করণীয় কী?", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                                Text("ক) দৌড়াদৌড়ি করা")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                            ) {
                                Text("খ) শান্তভাবে নিকটস্থ জরুরি বহির্গমন দিয়ে বের হওয়া")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                                Text("গ) কাজ চালিয়ে যাওয়া")
                            }
                        }
                    }
                }
            } else {
                // লিডারবোর্ড
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "শীর্ষ বিজয়ী তালিকা (বর্তমান মাস)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NavyPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "কর্মীদের নাম গোপনীয়তা রক্ষায় মাস্ক করে দেখানো হচ্ছে।",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    items(demoLeaderboard) { (rank, name, points) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = if (rank <= 3) AmberAccent else Color(0xFFEEEEEE),
                                        shape = RoundedCornerShape(20.dp)
                                    ) {
                                        Text(
                                            text = "#$rank",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (rank <= 3) Color.White else Color.Black,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                                Text("$points পয়েন্ট", fontWeight = FontWeight.Bold, color = NavyPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
