package com.example.facebookroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facebook_login_payload")
data class FacebookLoginPayloadEntity(
    @PrimaryKey val user_id: String,
    val access_token: String,
    val expiration_time: Long
)
