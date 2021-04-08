package com.example.githubsearchuser.repository

import android.accounts.NetworkErrorException
import com.example.githubsearchuser.data.*
import com.example.githubsearchuser.http.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {
    suspend fun <T> safeApiCallBack(apiCall: suspend () -> T
    ): ResponseResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResponseResult.Success(apiCall.invoke())
            } catch (e: Exception) {
                when (e) {
                    is HttpException ->
                        ResponseResult.GenericError(e.code())
                    is NetworkErrorException ->
                        ResponseResult.Failure(e)
                    else -> {
                        ResponseResult.Failure(e)
                    }
                }
            }
        }
    }
}

class UserRepository(private val userService: ApiService) : BaseRepository() {
    suspend fun getSearchUserResult(
        name: String,
        page: Int,
        pageSize: Int
    ) = safeApiCallBack {
        userService.searchUsersList(name, page, pageSize)
    }

    suspend fun getUserInfo(name: String) = safeApiCallBack {
        userService.getUserInfo(name)
    }

    suspend fun getUserRepos(
        name: String,
        page: Int,
        pageSize: Int
    ) = safeApiCallBack {
        userService.getUserRepos(name, page, pageSize)
    }
}

