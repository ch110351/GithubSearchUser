package com.example.githubsearchuser.viewmodel

import androidx.lifecycle.*
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.data.UserReposItem
import com.example.githubsearchuser.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class UserInfoViewModel(private val apiRepository: UserRepository) : ViewModel() {

    fun getUsers(name: String) = liveResponse {
        //get user's info
        val info = apiRepository.getUserInfo(name)
        //get user's repository
        val repos = apiRepository.getUserRepos(name, 1, 15)
        var reposList = mutableListOf<UserReposItem>()
        when (repos) {
            is ResponseResult.Success -> {
                reposList = repos.data
            }
            else -> {
            }
        }
        when (info) {
            is ResponseResult.Success -> {
                info.data.repos = reposList.toList()
            }
            else -> {
            }
        }
        info
    }
}

inline fun <T> liveResponse(crossinline body: suspend () -> ResponseResult<T>) =
    liveData(Dispatchers.IO) {
        emit(ResponseResult.Pending)
        val result = body()
        emit(result)
        emit(ResponseResult.Complete)
    }