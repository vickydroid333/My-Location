package com.ktt.mylocation.user

import com.ktt.mylocation.LocationTable
import kotlinx.coroutines.flow.Flow

class MyRepository(private val myDataBase: MyDataBase) {

    fun register(userName: MyTable)= myDataBase.studentDao().register(userName)

    fun insertLocation(locationTable: LocationTable)= myDataBase.studentDao().insertLocation(locationTable)

    fun verifyUserLogin(userName: String, password: String): Flow<MyTable?>? {
        return myDataBase.studentDao().verifyUserLogin(userName, password)
    }

    fun findByEmail(userName: String): Flow<MyTable?>? {
        return myDataBase.studentDao().findByEmail(userName)
    }

    fun getUserLocation(id:String): Flow<List<LocationTable>> {
        return myDataBase.studentDao().getUserLocation(id)
    }

}