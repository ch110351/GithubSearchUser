package com.example.githubsearchuser.di


import com.example.githubsearchuser.repository.ApiRepository
import com.example.githubsearchuser.repository.ApiRepositoryImpl
import com.example.githubsearchuser.ui.adapter.page.UsersListDataSourceFactory
import com.example.githubsearchuser.viewmodel.UserInfoViewModel
import com.example.githubsearchuser.viewmodel.UserSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val factoryModule = module {
    factory<ApiRepository> { (ApiRepositoryImpl()) }
}

val repositoryModule = module {
    viewModel {
        UserSearchViewModel(usersListDataSourceFactory = get())
    }

    viewModel {
        UserInfoViewModel(apiRepository = get())
    }
}

val userListSourceFactory = module {
    single { UsersListDataSourceFactory(apiRepository = get()) }
}