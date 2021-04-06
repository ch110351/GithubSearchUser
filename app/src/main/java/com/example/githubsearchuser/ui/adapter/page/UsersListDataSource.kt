package com.example.githubsearchuser.ui.adapter.page


import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.data.UserItem
import com.example.githubsearchuser.repository.ApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class UsersListDataSource(private val apiRepository: ApiRepository, private val username: String?) :
    PageKeyedDataSource<Int, UserItem>() {
    private val dataSourceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + dataSourceJob)
    var state: MutableLiveData<ResponseResult<Nothing>> = MutableLiveData()

    companion object {
        const val PAGE_SIZE = 15
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UserItem>
    ) {
        scope.launch {
            username?.let {
                updateState(ResponseResult.Pending)
                when (val response =
                    apiRepository.getSearchUserResult(it, 1, PAGE_SIZE)) {
                    is ResponseResult.Success -> {
                        updateState(ResponseResult.Complete)
                        callback.onResult(response.data.items, null, 2)
                    }
                    is ResponseResult.GenericError -> {
                        updateState(ResponseResult.GenericError(response.code))
                    }
                    is ResponseResult.Failure ->
                        updateState(ResponseResult.Failure(response.error))
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserItem>) {
        scope.launch {
            when (val response =
                username?.let { apiRepository.getSearchUserResult(it, params.key, PAGE_SIZE) }) {
                is ResponseResult.Success -> {
                    updateState(ResponseResult.Complete)
                    callback.onResult(response.data.items, params.key + 1)
                }
                is ResponseResult.GenericError -> {
                    updateState(ResponseResult.GenericError(response.code))
                }
                is ResponseResult.Failure ->
                    updateState(ResponseResult.Failure(response.error))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserItem>) {

    }

    /**
     * Update state
     */
    private fun updateState(state: ResponseResult<Nothing>) {
        this.state.postValue(state)
    }
}