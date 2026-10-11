package com.jinlite.workers.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson
import com.jinlite.workers.data.local.*
import com.jinlite.workers.data.remote.JinliteApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class WorkerRepository(
    private val context: Context,
    private val database: AppDatabase = AppDatabase.getDatabase(context),
    private val api: JinliteApiService = JinliteApiService.create()
) {
    private val noticeDao = database.noticeDao()
    private val holidayDao = database.holidayDao()
    private val ruleDao = database.ruleDao()
    private val grievanceDao = database.grievanceDao()
    private val gson = Gson()

    // --- ১. অফলাইন ফার্স্ট নোটিশ ---
    fun getNoticesFlow(plant: String = "ALL", floor: String = "ALL", side: String = "ALL"): Flow<List<NoticeEntity>> {
        return if (plant == "ALL" && floor == "ALL" && side == "ALL") {
            noticeDao.getAllNotices()
        } else {
            noticeDao.getFilteredNotices(plant, floor, side)
        }
    }

    suspend fun syncNotices(plant: String = "", floor: String = "", side: String = ""): Result<Int> = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable()) {
            return@withContext Result.failure(Exception("অফলাইন মোড: ইন্টারনেট সংযোগ নেই"))
        }

        try {
            val response = api.getNotices(plant = plant, floor = floor, side = side)
            if (response.success && response.data != null) {
                val entities = response.data.map { dto ->
                    val attach = dto.attachments?.firstOrNull()
                    NoticeEntity(
                        noticeId = dto.noticeId,
                        titleBn = dto.titleBn,
                        titleEn = dto.titleEn,
                        descriptionBn = dto.descriptionBn,
                        category = dto.category ?: "GENERAL",
                        priority = dto.priority ?: "NORMAL",
                        targetType = dto.targetType ?: "ALL",
                        targetPlant = dto.targetPlant ?: "ALL",
                        targetFloor = dto.targetFloor ?: "ALL",
                        targetSide = dto.targetSide ?: "ALL",
                        status = dto.status ?: "PUBLISHED",
                        publishedAt = dto.publishedAt ?: "",
                        attachmentUrl = attach?.viewUrl,
                        cachedAt = System.currentTimeMillis()
                    )
                }
                noticeDao.insertNotices(entities)
                Result.success(entities.size)
            } else {
                Result.failure(Exception(response.message ?: "নোটিশ সিঙ্ক ব্যর্থ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- ২. অফলাইন ফার্স্ট ছুটির তালিকা ---
    fun getHolidaysFlow(): Flow<List<HolidayEntity>> = holidayDao.getAllHolidays()

    suspend fun syncHolidays(): Result<Int> = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable()) return@withContext Result.failure(Exception("ইন্টারনেট সংযোগ নেই"))
        try {
            val res = api.getHolidays()
            if (res.success && res.data != null) {
                val entities = res.data.map { dto ->
                    HolidayEntity(
                        holidayId = dto.holidayId,
                        titleBn = dto.titleBn,
                        holidayDate = dto.holidayDate,
                        daysCount = (dto.daysCount?.toString()?.toIntOrNull()) ?: 1,
                        descriptionBn = dto.descriptionBn
                    )
                }
                holidayDao.insertHolidays(entities)
                Result.success(entities.size)
            } else {
                Result.failure(Exception(res.message ?: "ছুটি সিঙ্ক ব্যর্থ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- ৩. অফলাইন ফার্স্ট কারখানা রুলস ---
    fun getRulesFlow(): Flow<List<CompanyRuleEntity>> = ruleDao.getAllRules()

    suspend fun syncRules(): Result<Int> = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable()) return@withContext Result.failure(Exception("ইন্টারনেট সংযোগ নেই"))
        try {
            val res = api.getCompanyRules()
            if (res.success && res.data != null) {
                val entities = res.data.map { dto ->
                    CompanyRuleEntity(
                        ruleId = dto.ruleId,
                        titleBn = dto.titleBn,
                        category = dto.category ?: "GENERAL",
                        contentBn = dto.contentBn,
                        version = dto.version ?: "1.0"
                    )
                }
                ruleDao.insertRules(entities)
                Result.success(entities.size)
            } else {
                Result.failure(Exception(res.message ?: "রুলস সিঙ্ক ব্যর্থ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- ৪. এক ক্লিকে সম্পূর্ণ অফলাইন সিঙ্ক (যখনই অনলাইন হবে) ---
    suspend fun syncAllData(plant: String = "", floor: String = "", side: String = ""): Boolean = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable()) return@withContext false
        try {
            syncNotices(plant, floor, side)
            syncHolidays()
            syncRules()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --- ৫. অভিযোগ সাবমিশন (পরিচয় গোপন বা প্রকাশ করে) ---
    fun getMyGrievancesFlow(): Flow<List<GrievanceEntity>> = grievanceDao.getMyGrievances()

    suspend fun submitGrievance(
        workerId: String,
        isAnonymous: Boolean,
        plant: String,
        floor: String,
        side: String,
        category: String,
        subject: String,
        details: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable()) {
            return@withContext Result.failure(Exception("অভিযোগ জমা দিতে ইন্টারনেট সংযোগ প্রয়োজন।"))
        }

        try {
            val payload = mapOf(
                "action" to "submitGrievance",
                "worker_id" to if (isAnonymous) "ANONYMOUS" else workerId,
                "is_anonymous" to isAnonymous,
                "plant" to plant,
                "floor" to floor,
                "side" to side,
                "category" to category,
                "subject_bn" to subject,
                "details_bn" to details
            )

            val jsonBody = gson.toJson(payload).toRequestBody("application/json".toMediaType())
            val res = api.postAction(jsonBody)

            if (res.success && res.data != null) {
                // Tracking code save to local DB
                val dataMap = res.data as? Map<*, *>
                val trackingCode = dataMap?.get("tracking_code")?.toString() ?: "JLG-PENDING"

                val entity = GrievanceEntity(
                    trackingCode = trackingCode,
                    workerId = if (isAnonymous) "ANONYMOUS" else workerId,
                    plant = plant,
                    floor = floor,
                    side = side,
                    subjectBn = subject,
                    detailsBn = details,
                    status = "SUBMITTED",
                    createdAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
                )
                grievanceDao.insertGrievance(entity)

                Result.success(trackingCode)
            } else {
                Result.failure(Exception(res.message ?: "অভিযোগ দাখিল ব্যর্থ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ইন্টারনেট সংযোগ চেক হেল্পার
    fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
