package com.jinlite.workers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinlite.workers.data.local.GrievanceEntity
import com.jinlite.workers.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrievanceScreen(
    savedGrievances: List<GrievanceEntity>,
    onBack: () -> Unit,
    onSubmit: (isAnonymous: Boolean, plant: String, floor: String, side: String, category: String, subject: String, details: String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Form state
    var isAnonymous by remember { mutableStateOf(true) }
    var selectedPlant by remember { mutableStateOf("Plant 1") }
    var selectedFloor by remember { mutableStateOf("Floor 1") }
    var selectedSide by remember { mutableStateOf("Side A") }
    var selectedCategory by remember { mutableStateOf("WORKPLACE") }
    var subject by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }

    val plants = listOf("Plant 1", "Plant 2", "Plant 3", "Plant 4", "Plant 5", "Plant 6", "Plant 7")
    val floors = listOf("Floor 1", "Floor 2", "Floor 3", "Floor 4", "Floor 5")
    val sides = listOf("Side A", "Side B")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("অভিযোগ ও পরামর্শ", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
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
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("নতুন অভিযোগ দাখিল", fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("আমার অভিযোগসমূহ (${savedGrievances.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                )
            }

            if (selectedTab == 0) {
                // ফর্ম
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // পরিচয় গোপন টগল
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isAnonymous) Color(0xFFEDE7F6) else Color(0xFFE3F2FD)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Anonymous",
                                    tint = if (isAnonymous) Color(0xFF512DA8) else NavyPrimary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isAnonymous) "পরিচয় গোপন রাখা হবে (বেনামী)" else "পরিচয় প্রকাশ করা হবে",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = if (isAnonymous) "আপনার নাম বা কার্ড নম্বর কারো কাছে প্রকাশ করা হবে না।" else "আপনার কার্ড নম্বর অ্যাডমিন দেখতে পাবেন।",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                }
                                Switch(
                                    checked = isAnonymous,
                                    onCheckedChange = { isAnonymous = it }
                                )
                            }
                        }
                    }

                    // প্ল্যান্ট, ফ্লোর ও সাইড
                    item {
                        Text("কারখানার অবস্থান (কোথায় সমস্যা?)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SimpleDropdown(
                                label = "প্ল্যান্ট",
                                items = plants,
                                selected = selectedPlant,
                                onSelect = { selectedPlant = it },
                                modifier = Modifier.weight(1f)
                            )
                            SimpleDropdown(
                                label = "ফ্লোর",
                                items = floors,
                                selected = selectedFloor,
                                onSelect = { selectedFloor = it },
                                modifier = Modifier.weight(1f)
                            )
                            SimpleDropdown(
                                label = "সাইড",
                                items = sides,
                                selected = selectedSide,
                                onSelect = { selectedSide = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // বিষয়
                    item {
                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("অভিযোগের বিষয় *") },
                            placeholder = { Text("যেমন: ফ্লোরে খাবার পানির সংকট") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // বিস্তারিত বিবরণ
                    item {
                        OutlinedTextField(
                            value = details,
                            onValueChange = { details = it },
                            label = { Text("বিস্তারিত বিবরণ *") },
                            placeholder = { Text("সমস্যাটি বিস্তারিতভাবে সহজ ভাষায় লিখুন...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // সাবমিট বাটন
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                if (subject.isNotBlank() && details.isNotBlank()) {
                                    onSubmit(isAnonymous, selectedPlant, selectedFloor, selectedSide, selectedCategory, subject, details)
                                    subject = ""
                                    details = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            enabled = subject.isNotBlank() && details.isNotBlank()
                        ) {
                            Text("অভিযোগ জমা দিন", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // অফলাইনে সংরক্ষিত অভিযোগের তালিকা
                if (savedGrievances.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("আপনার কোনো অভিযোগ দাখিল করা নেই।", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(savedGrievances) { g ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "কোড: ${g.trackingCode}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = NavyPrimary
                                        )
                                        Surface(
                                            color = if (g.status == "RESOLVED") Color(0xFFC8E6C9) else Color(0xFFFFF9C4),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = g.status,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (g.status == "RESOLVED") Color(0xFF1B5E20) else Color(0xFFE65100),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = g.subjectBn, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = g.detailsBn, fontSize = 13.sp, color = Color.DarkGray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${g.plant} • ${g.floor} • ${g.side} • ${g.createdAt}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    label: String,
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, fontSize = 11.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontSize = 13.sp) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
