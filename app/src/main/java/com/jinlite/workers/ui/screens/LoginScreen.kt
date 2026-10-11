package com.jinlite.workers.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinlite.workers.ui.theme.AmberAccent
import com.jinlite.workers.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    isLoading: Boolean,
    onLogin: (cardNo: String, phone: String) -> Unit,
    onRegister: (cardNo: String, phone: String, name: String, plant: String, floor: String, side: String, designation: String) -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // ফিল্ড স্টেট
    var cardNo by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var selectedPlant by remember { mutableStateOf("Plant 1") }
    var selectedFloor by remember { mutableStateOf("Floor 1") }
    var selectedSide by remember { mutableStateOf("Side A") }
    var selectedDesignation by remember { mutableStateOf("অপারেটর") }

    val plantList = listOf("Plant 1", "Plant 2", "Plant 3", "Plant 4", "Plant 5", "Plant 6", "Plant 7")
    val floorList = listOf("Ground Floor", "Floor 1", "Floor 2", "Floor 3", "Floor 4")
    val sideList = listOf("Side A", "Side B", "Both Sides")
    val designationList = listOf("অপারেটর", "হেল্পার", "কোয়ালিটি ইন্সপেক্টর", "লাইন সুপারভাইজার", "টেকনিশিয়ান")

    var plantExpanded by remember { mutableStateOf(false) }
    var floorExpanded by remember { mutableStateOf(false) }
    var sideExpanded by remember { mutableStateOf(false) }
    var designationExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // অ্যাপ ব্র্যান্ড হেডার
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(NavyPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Factory,
                contentDescription = null,
                tint = AmberAccent,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "জিনলাইট গার্মেন্টস",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = NavyPrimary
        )
        Text(
            text = "শ্রমিক ও কর্মচারী কমিউনিকেশন পোর্টাল",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        // লগইন ও রেজিস্ট্রেশন মোড সুইচ ট্যাব
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { isRegisterMode = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isRegisterMode) NavyPrimary else Color.Transparent,
                        contentColor = if (!isRegisterMode) Color.White else Color.DarkGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("লগইন", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { isRegisterMode = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRegisterMode) NavyPrimary else Color.Transparent,
                        contentColor = if (isRegisterMode) Color.White else Color.DarkGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("নতুন অ্যাকাউন্ট", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ইনপুট কার্ড
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                // কার্ড নম্বর
                OutlinedTextField(
                    value = cardNo,
                    onValueChange = { cardNo = it },
                    label = { Text("কার্ড নম্বর / আইডি (যেমন: 1042)") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = NavyPrimary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // মোবাইল নম্বর
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("মোবাইল নম্বর (যেমন: 017XXXXXXXX)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = NavyPrimary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // যদি রেজিস্ট্রেশন মোড থাকে তবে অতিরিক্ত ফিল্ড
                AnimatedVisibility(visible = isRegisterMode) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))

                        // পূর্ণ নাম
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("আপনার পূর্ণ নাম (বাংলায়)") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // প্ল্যান্ট সিলেক্টর
                        ExposedDropdownMenuBox(
                            expanded = plantExpanded,
                            onExpandedChange = { plantExpanded = !plantExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedPlant,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("প্ল্যান্ট / ফ্যাক্টরি") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = plantExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = plantExpanded,
                                onDismissRequest = { plantExpanded = false }
                            ) {
                                plantList.forEach { p ->
                                    DropdownMenuItem(
                                        text = { Text(p) },
                                        onClick = {
                                            selectedPlant = p
                                            plantExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // ফ্লোর এবং সাইড পাশাপাশি
                        Row(modifier = Modifier.fillMaxWidth()) {
                            ExposedDropdownMenuBox(
                                expanded = floorExpanded,
                                onExpandedChange = { floorExpanded = !floorExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedFloor,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("ফ্লোর") },
                                    modifier = Modifier.menuAnchor().fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = floorExpanded,
                                    onDismissRequest = { floorExpanded = false }
                                ) {
                                    floorList.forEach { f ->
                                        DropdownMenuItem(
                                            text = { Text(f) },
                                            onClick = {
                                                selectedFloor = f
                                                floorExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            ExposedDropdownMenuBox(
                                expanded = sideExpanded,
                                onExpandedChange = { sideExpanded = !sideExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedSide,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("সাইড") },
                                    modifier = Modifier.menuAnchor().fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = sideExpanded,
                                    onDismissRequest = { sideExpanded = false }
                                ) {
                                    sideList.forEach { s ->
                                        DropdownMenuItem(
                                            text = { Text(s) },
                                            onClick = {
                                                selectedSide = s
                                                sideExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // পদবী
                        ExposedDropdownMenuBox(
                            expanded = designationExpanded,
                            onExpandedChange = { designationExpanded = !designationExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedDesignation,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("পদবী") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = designationExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = designationExpanded,
                                onDismissRequest = { designationExpanded = false }
                            ) {
                                designationList.forEach { d ->
                                    DropdownMenuItem(
                                        text = { Text(d) },
                                        onClick = {
                                            selectedDesignation = d
                                            designationExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // সাবমিট বাটন
                Button(
                    onClick = {
                        if (isRegisterMode) {
                            onRegister(cardNo, phone, fullName, selectedPlant, selectedFloor, selectedSide, selectedDesignation)
                        } else {
                            onLogin(cardNo, phone)
                        }
                    },
                    enabled = !isLoading && cardNo.isNotBlank() && phone.isNotBlank() && (!isRegisterMode || fullName.isNotBlank()),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = if (isRegisterMode) "অ্যাকাউন্ট তৈরি করুন" else "অ্যাপে প্রবেশ করুন",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "মংলা ইপিজেড, বাগেরহাট • হেল্পলাইন: ০১৭০০-০০০০০০",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
