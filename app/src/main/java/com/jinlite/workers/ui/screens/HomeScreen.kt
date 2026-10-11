package com.jinlite.workers.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinlite.workers.data.local.NoticeEntity
import com.jinlite.workers.ui.components.OfflineBanner
import com.jinlite.workers.ui.components.PriorityBadge
import com.jinlite.workers.ui.components.TargetBadge
import com.jinlite.workers.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    notices: List<NoticeEntity>,
    isOffline: Boolean,
    onRefresh: () -> Unit,
    onNavigateToNotices: () -> Unit,
    onNavigateToGrievances: () -> Unit,
    onNavigateToHolidays: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNoticeClick: (NoticeEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "JINLITE WORKERS",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "মংলা ইপিজেড, বাংলাদেশ",
                            fontSize = 12.sp,
                            color = Color(0xFFBBDEFB)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary),
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // অফলাইন ব্যানার (যদি ইন্টারনেট না থাকে)
            OfflineBanner(isOffline = isOffline)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ১. কুইক মেনু গ্রিড (Quick Actions)
                item {
                    Text(
                        text = "প্রয়োজনীয় সেবা",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "নোটিশ বোর্ড",
                            icon = Icons.Default.Campaign,
                            color = Color(0xFF1976D2),
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToNotices
                        )
                        QuickActionCard(
                            title = "ছুটি ও নিয়ম",
                            icon = Icons.Default.CalendarMonth,
                            color = Color(0xFF388E3C),
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToHolidays
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionCard(
                            title = "অভিযোগ ও মতামত",
                            icon = Icons.Default.Feedback,
                            color = Color(0xFFD32F2F),
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToGrievances
                        )
                        QuickActionCard(
                            title = "কুইজ ও পয়েন্ট",
                            icon = Icons.Default.EmojiEvents,
                            color = Color(0xFFF57C00),
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToQuiz
                        )
                    }
                }

                // ২. সর্বশেষ নোটিশ সেকশন (অফলাইনেও সেভ করা থাকবে)
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "সর্বশেষ নোটিশসমূহ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        TextButton(onClick = onNavigateToNotices) {
                            Text("সবগুলো দেখুন", fontSize = 13.sp)
                        }
                    }
                }

                if (notices.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isOffline) "অফলাইনে কোনো নোটিশ সংরক্ষিত নেই। অনলাইনে এলে স্বয়ংক্রিয়ভাবে সেভ হবে।" else "কোনো নতুন নোটিশ নেই।",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } else {
                    items(notices.take(5)) { notice ->
                        NoticeCard(notice = notice, onClick = { onNoticeClick(notice) })
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
        }
    }
}

@Composable
fun NoticeCard(
    notice: NoticeEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TargetBadge(
                    targetType = notice.targetType,
                    plant = notice.targetPlant,
                    floor = notice.targetFloor,
                    side = notice.targetSide
                )
                PriorityBadge(priority = notice.priority)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notice.titleBn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = notice.descriptionBn,
                fontSize = 13.sp,
                color = Color(0xFF616161),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notice.publishedAt.take(10),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                if (!notice.attachmentUrl.isNullOrEmpty()) {
                    Text(
                        text = "📎 ফাইল সংযুক্ত",
                        fontSize = 11.sp,
                        color = NavyPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
