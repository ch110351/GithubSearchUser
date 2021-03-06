package com.example.githubsearchuser.http.api


import com.example.githubsearchuser.data.SearchUserResponse
import com.example.githubsearchuser.data.UserInfo
import com.example.githubsearchuser.data.UserRepos
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * github api, get user info
     */
    @GET("users/{login}")
    suspend  fun getUserInfo(
        @Path("login") login: String
    ): UserInfo

    @GET("users/{login}/repos")
    suspend  fun getUserRepos(
        @Path("login") login: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): UserRepos

    /**
     * github api, search user by login
     */
    @GET("search/users")
    suspend  fun searchUsersList(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): SearchUserResponse
}