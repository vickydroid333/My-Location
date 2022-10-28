package com.ktt.mylocation.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class MyTable (
    val user_name: String,
    val email:String,
    val password:String,
    val confirm_password:String,
    @PrimaryKey(autoGenerate = true)
    val id :Int=0
)