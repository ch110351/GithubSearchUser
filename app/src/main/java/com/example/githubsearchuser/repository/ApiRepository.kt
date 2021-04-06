package com.example.githubsearchuser.repository

import android.accounts.NetworkErrorException
import com.example.githubsearchuser.http.tool.RetrofitBuilder.apiService
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.data.SearchUserResponse
import com.example.githubsearchuser.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

interface ApiRepository {
    suspend fun getSearchUserResult(
        name: String,
        page: Int,
        pageSize: Int
    ): ResponseResult<SearchUserResponse>

    suspend fun getUserInfo(name: String): ResponseResult<UserInfo>
}

class ApiRepositoryImpl : ApiRepository {
    override suspend fun getSearchUserResult(
        name: String,
        page: Int,
        pageSize: Int
    ): ResponseResult<SearchUserResponse> = safeApiCallBack {
        apiService.searchUsersList(name, page, pageSize)
    }

    override suspend fun getUserInfo(name: String): ResponseResult<UserInfo> = safeApiCallBack {
        apiService.getUserInfo(name)
    }
}

suspend fun <T> safeApiCallBack(apiCall: suspend () -> T): ResponseResult<T> {
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