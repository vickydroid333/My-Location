package com.ktt.mylocation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.mylocation.LocationTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MyViewModel(private val repository: MyRepository) : ViewModel() {

    fun verifyUserLogin(username: String, password: String): Flow<MyTable?>? {

        val readAllData = repository.verifyUserLogin(username, password)
        return readAllData
    }

    fun findByEmail(userName: String): Flow<MyTable?>? {
        val readAllData = repository.findByEmail(userName)
        return readAllData
    }

    fun register(userName: MyTable) = viewModelScope.launch {
        repository.register(userName)
    }

    fun insertLocation(locationTable: LocationTable) = viewModelScope.launch {
        repository.insertLocation(locationTable)
    }

    fun getUserLocation(id: String): Flow<List<LocationTable>> {

        return repository.getUserLocation(id)
    }
}