package com.example.githubsearchuser.ui.adapter.page

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.githubsearchuser.data.UserItem
import com.example.githubsearchuser.repository.ApiRepository

class UsersListDataSourceFactory(
    private val apiRepository: ApiRepository
) : DataSource.Factory<Int, UserItem>() {

    private var username: String? = null
    private var usersListDataSource: UsersListDataSource? = null
    val dataSourceLiveData: MutableLiveData<UsersListDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, UserItem> {
        usersListDataSource = UsersListDataSource(apiRepository, username)

       dataSourceLiveData.postValue(usersListDataSource)

        return usersListDataSource!!
    }

    fun setUserName(username: String) {
        this.username = username
    }

    fun clearDataSource() {
        if (usersListDataSource != null)
            usersListDataSource?.invalidate()
    }


}