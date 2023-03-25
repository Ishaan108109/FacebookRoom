package com.example.facebookroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FacebookLoginPayloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(payload: FacebookLoginPayloadEntity)

    @Query("SELECT * FROM facebook_login_payload WHERE user_id = :userId")
    fun getPayload(userId: String): FacebookLoginPayloadEntity?

    @Query("DELETE FROM facebook_login_payload WHERE user_id = :userId")
    fun deletePayload(userId: String)
}
