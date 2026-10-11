package com.jinlite.workers.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notices")
data class NoticeEntity(
    @PrimaryKey
    val noticeId: String,
    val titleBn: String,
    val titleEn: String? = null,
    val descriptionBn: String,
    val category: String,
    val priority: String,
    val targetType: String,
    val targetPlant: String,
    val targetFloor: String,
    val targetSide: String,
    val status: String,
    val publishedAt: String,
    val attachmentUrl: String? = null,
    val voiceUrl: String? = null,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey
    val holidayId: String,
    val titleBn: String,
    val holidayDate: String,
    val daysCount: Int = 1,
    val descriptionBn: String? = null,
    val targetPlant: String? = null,
    val targetFloor: String? = null,
    val targetSide: String? = null
)

@Entity(tableName = "company_rules")
data class CompanyRuleEntity(
    @PrimaryKey
    val ruleId: String,
    val titleBn: String,
    val category: String,
    val contentBn: String,
    val version: String = "1.0"
)

@Entity(tableName = "my_grievances")
data class GrievanceEntity(
    @PrimaryKey
    val trackingCode: String,
    val workerId: String,
    val plant: String,
    val floor: String,
    val side: String,
    val subjectBn: String,
    val detailsBn: String,
    val status: String,
    val createdAt: String
)
