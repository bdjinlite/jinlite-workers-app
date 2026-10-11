package com.jinlite.workers.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notices ORDER BY publishedAt DESC")
    fun getAllNotices(): Flow<List<NoticeEntity>>

    @Query("""
        SELECT * FROM notices 
        WHERE targetType = 'ALL' 
           OR (targetType = 'PLANT' AND (targetPlant = 'ALL' OR targetPlant = :plant))
           OR (targetType = 'FLOOR' AND (targetPlant = 'ALL' OR targetPlant = :plant) AND (targetFloor = 'ALL' OR targetFloor = :floor))
           OR (targetType = 'SIDE' AND (targetPlant = 'ALL' OR targetPlant = :plant) AND (targetFloor = 'ALL' OR targetFloor = :floor) AND (targetSide = 'ALL' OR targetSide = :side))
        ORDER BY publishedAt DESC
    """)
    fun getFilteredNotices(plant: String, floor: String, side: String): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<NoticeEntity>)

    @Query("DELETE FROM notices")
    suspend fun clearNotices()
}

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holidays ORDER BY holidayDate ASC")
    fun getAllHolidays(): Flow<List<HolidayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolidays(holidays: List<HolidayEntity>)
}

@Dao
interface RuleDao {
    @Query("SELECT * FROM company_rules ORDER BY ruleId ASC")
    fun getAllRules(): Flow<List<CompanyRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRules(rules: List<CompanyRuleEntity>)
}

@Dao
interface GrievanceDao {
    @Query("SELECT * FROM my_grievances ORDER BY createdAt DESC")
    fun getMyGrievances(): Flow<List<GrievanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrievance(grievance: GrievanceEntity)

    @Query("UPDATE my_grievances SET status = :status WHERE trackingCode = :trackingCode")
    suspend fun updateStatus(trackingCode: String, status: String)
}
