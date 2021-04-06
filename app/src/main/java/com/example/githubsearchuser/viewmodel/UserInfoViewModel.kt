package com.example.githubsearchuser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserInfoViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun getUsers(name: String) = liveResponse {
        apiRepository.getUserInfo(name)
    }
}

inline fun <T> liveResponse(crossinline body: suspend () -> ResponseResult<T>) =
    liveData(Dispatchers.IO) {
        emit(ResponseResult.Pending)
        val result = body()
        emit(result)
        emit(ResponseResult.Complete)
    }