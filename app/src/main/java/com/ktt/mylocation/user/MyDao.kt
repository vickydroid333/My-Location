package com.ktt.mylocation.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ktt.mylocation.LocationTable
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDao {

    @Query("SELECT * FROM user_table where user_name LIKE  :userName")
    fun findByEmail(userName: String?): Flow<MyTable?>?

    @Insert
    fun register(user: MyTable)

    @Insert
    fun insertLocation(location: LocationTable)

    @Query("select * from location_table where userId like :id ")
    fun getUserLocation(id: String): Flow<List<LocationTable>>

    @Query("SELECT * FROM user_table where user_name LIKE  :userName AND password LIKE :password")
    fun verifyUserLogin(userName: String?, password: String?): Flow<MyTable?>?

}