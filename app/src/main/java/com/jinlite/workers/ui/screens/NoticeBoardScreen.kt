package com.jinlite.workers.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinlite.workers.data.local.NoticeEntity
import com.jinlite.workers.ui.components.PriorityBadge
import com.jinlite.workers.ui.components.TargetBadge
import com.jinlite.workers.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeBoardScreen(
    notices: List<NoticeEntity>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }
    var selectedNotice by remember { mutableStateOf<NoticeEntity?>(null) }

    val categories = listOf("ALL" to "সকল", "GENERAL" to "সাধারণ", "HOLIDAY" to "ছুটি", "URGENT" to "জরুরি", "SAFETY" to "নিরাপত্তা")

    val filteredNotices = notices.filter { notice ->
        val matchesCategory = when (selectedCategory) {
            "ALL" -> true
            "URGENT" -> notice.priority.uppercase() == "URGENT" || notice.priority.uppercase() == "HIGH"
            else -> notice.category.uppercase() == selectedCategory
        }
        val matchesSearch = searchQuery.isBlank() ||
                notice.titleBn.contains(searchQuery, ignoreCase = true) ||
                notice.descriptionBn.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("নোটিশ বোর্ড", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavyPrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // সার্চ বার
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("নোটিশ খুঁজুন...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            // ফিল্টার চিপস
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (key, label) ->
                    FilterChip(
                        selected = selectedCategory == key,
                        onClick = { selectedCategory = key },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            // নোটিশ তালিকা
            if (filteredNotices.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("কোনো নোটিশ পাওয়া যায়নি।", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredNotices) { notice ->
                        NoticeCard(notice = notice, onClick = { selectedNotice = notice })
                    }
                }
            }
        }
    }

    // নোটিশ বিস্তারিত ডায়ালগ
    selectedNotice?.let { notice ->
        AlertDialog(
            onDismissRequest = { selectedNotice = null },
            title = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TargetBadge(notice.targetType, notice.targetPlant, notice.targetFloor, notice.targetSide)
                        PriorityBadge(notice.priority)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(notice.titleBn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(notice.descriptionBn, fontSize = 14.sp, color = Color(0xFF333333))
                    Spacer(modifier = Modifier.height(16.dp))

                    // অ্যাটাচমেন্ট ওপেনার
                    if (!notice.attachmentUrl.isNullOrEmpty()) {
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.attachmentUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) { }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                        ) {
                            Icon(Icons.Default.AttachFile, contentDescription = "Attachment")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("সংযুক্ত ফাইল / ছবি দেখুন")
                        }
                    }

                    // অডিও ঘোষণা
                    if (!notice.voiceUrl.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.voiceUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) { }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Voice")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ভয়েস ঘোষণা শুনুন")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedNotice = null }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }
}
