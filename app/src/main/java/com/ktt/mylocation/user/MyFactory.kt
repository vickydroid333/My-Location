package com.ktt.mylocation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyFactory(private val repository: MyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModel(repository) as T
    }


}