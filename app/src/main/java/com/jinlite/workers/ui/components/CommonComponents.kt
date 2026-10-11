package com.jinlite.workers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinlite.workers.ui.theme.RedUrgent

@Composable
fun OfflineBanner(
    isOffline: Boolean,
    modifier: Modifier = Modifier
) {
    if (isOffline) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF3E0))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = "Offline",
                tint = Color(0xFFE65100),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "আপনি অফলাইনে আছেন। সেভ করা তথ্য দেখানো হচ্ছে।",
                color = Color(0xFFE65100),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PriorityBadge(priority: String) {
    val (bgColor, textColor, text) = when (priority.uppercase()) {
        "URGENT" -> Triple(Color(0xFFFFCDD2), RedUrgent, "অত্যন্ত জরুরি")
        "HIGH" -> Triple(Color(0xFFFFE082), Color(0xFFE65100), "জরুরি")
        else -> Triple(Color(0xFFE1F5FE), Color(0xFF0277BD), "সাধারণ")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun TargetBadge(targetType: String, plant: String, floor: String, side: String) {
    val label = when (targetType.uppercase()) {
        "PLANT" -> plant
        "FLOOR" -> "$plant > $floor"
        "SIDE" -> "$plant > $floor > $side"
        else -> "সকল কর্মী"
    }

    Surface(
        color = Color(0xFFEDE7F6),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF512DA8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
