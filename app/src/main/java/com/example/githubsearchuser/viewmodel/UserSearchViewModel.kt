package com.example.githubsearchuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.data.UserItem
import com.example.githubsearchuser.ui.adapter.page.UsersListDataSource
import com.example.githubsearchuser.ui.adapter.page.UsersListDataSource.Companion.PAGE_SIZE
import com.example.githubsearchuser.ui.adapter.page.UsersListDataSourceFactory

class UserSearchViewModel(private val usersListDataSourceFactory: UsersListDataSourceFactory) :
    ViewModel() {
    lateinit var pagedList: LiveData<PagedList<UserItem>>

    init {
        initUsersListFactory()
    }

    private fun initUsersListFactory() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(5)
            .build()
        pagedList = LivePagedListBuilder(usersListDataSourceFactory, config)
            .build()
    }

    fun clearDataSource() {
        usersListDataSourceFactory.clearDataSource()
    }

    fun setSearchUserName(username: String) {
        usersListDataSourceFactory.setUserName(username)
    }

    fun getState(): LiveData<ResponseResult<Nothing>> =
        Transformations.switchMap<UsersListDataSource, ResponseResult<Nothing>>(
            usersListDataSourceFactory.dataSourceLiveData,
            UsersListDataSource::state
        )

}
