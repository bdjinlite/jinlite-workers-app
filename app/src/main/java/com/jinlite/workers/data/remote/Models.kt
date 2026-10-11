package com.jinlite.workers.data.remote

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?
)

data class NoticeDto(
    @SerializedName("notice_id") val noticeId: String,
    @SerializedName("title_bn") val titleBn: String,
    @SerializedName("title_en") val titleEn: String?,
    @SerializedName("description_bn") val descriptionBn: String,
    @SerializedName("category") val category: String?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("target_type") val targetType: String?,
    @SerializedName("target_plant") val targetPlant: String?,
    @SerializedName("target_floor") val targetFloor: String?,
    @SerializedName("target_side") val targetSide: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?
)

data class AttachmentDto(
    @SerializedName("attachment_id") val attachmentId: String,
    @SerializedName("file_name") val fileName: String?,
    @SerializedName("file_type") val fileType: String?,
    @SerializedName("view_url") val viewUrl: String?
)

data class HolidayDto(
    @SerializedName("holiday_id") val holidayId: String,
    @SerializedName("title_bn") val titleBn: String,
    @SerializedName("holiday_date") val holidayDate: String,
    @SerializedName("days_count") val daysCount: Any?,
    @SerializedName("description_bn") val descriptionBn: String?
)

data class CompanyRuleDto(
    @SerializedName("rule_id") val ruleId: String,
    @SerializedName("title_bn") val titleBn: String,
    @SerializedName("category") val category: String?,
    @SerializedName("content_bn") val contentBn: String,
    @SerializedName("version") val version: String?
)

data class GrievanceSubmitResponse(
    @SerializedName("tracking_code") val trackingCode: String,
    @SerializedName("status") val status: String
)

data class LeaderboardItemDto(
    @SerializedName("worker_id") val workerId: String,
    @SerializedName("masked_name") val maskedName: String,
    @SerializedName("plant") val plant: String?,
    @SerializedName("points") val points: Int,
    @SerializedName("rank") val rank: Int
)

data class QuizDto(
    @SerializedName("quiz_id") val quizId: String,
    @SerializedName("title_bn") val titleBn: String,
    @SerializedName("description_bn") val descriptionBn: String?,
    @SerializedName("points_per_question") val pointsPerQuestion: Int,
    @SerializedName("questions") val questions: List<QuizQuestionDto>?
)

data class QuizQuestionDto(
    @SerializedName("question_id") val questionId: String,
    @SerializedName("question_bn") val questionBn: String,
    @SerializedName("options") val options: List<QuizOptionDto>
)

data class QuizOptionDto(
    @SerializedName("option_id") val optionId: String,
    @SerializedName("option_bn") val optionBn: String
)

data class QuizSubmitResponse(
    @SerializedName("score") val score: Int,
    @SerializedName("earned_points") val earnedPoints: Int
)
