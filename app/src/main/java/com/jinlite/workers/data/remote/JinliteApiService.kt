package com.jinlite.workers.data.remote

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface JinliteApiService {

    @GET("exec")
    suspend fun getNotices(
        @Query("action") action: String = "getNotices",
        @Query("plant") plant: String? = null,
        @Query("floor") floor: String? = null,
        @Query("side") side: String? = null
    ): ApiResponse<List<NoticeDto>>

    @GET("exec")
    suspend fun getHolidays(
        @Query("action") action: String = "getHolidays"
    ): ApiResponse<List<HolidayDto>>

    @GET("exec")
    suspend fun getCompanyRules(
        @Query("action") action: String = "getCompanyRules"
    ): ApiResponse<List<CompanyRuleDto>>

    @GET("exec")
    suspend fun getAvailableQuizzes(
        @Query("action") action: String = "getAvailableQuizzes"
    ): ApiResponse<List<QuizDto>>

    @GET("exec")
    suspend fun getLeaderboard(
        @Query("action") action: String = "getLeaderboard"
    ): ApiResponse<List<LeaderboardItemDto>>

    @POST("exec")
    suspend fun postAction(
        @Body body: RequestBody
    ): ApiResponse<Any>

    companion object {
        // লাইভ ডিপ্লয়ড গুগল অ্যাপস স্ক্রিপ্ট বেস ইউআরএল
        const val BASE_URL = "https://script.google.com/macros/s/AKfycbyVQc52epCp_2RS7KDYIjoyJZBWl2uxhQB6eC6LIKhiRV7USP3-1MSbwOngPpLul82J/"

        fun create(): JinliteApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JinliteApiService::class.java)
        }
    }
}
