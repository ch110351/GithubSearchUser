package com.example.githubsearchuser.di


import com.example.githubsearchuser.BuildConfig
import com.example.githubsearchuser.common.Constant
import com.example.githubsearchuser.common.Constant.Companion.TIME_OUT
import com.example.githubsearchuser.http.api.ApiService
import com.example.githubsearchuser.repository.UserRepository
import com.example.githubsearchuser.ui.adapter.page.UsersListDataSourceFactory
import com.example.githubsearchuser.viewmodel.UserInfoViewModel
import com.example.githubsearchuser.viewmodel.UserSearchViewModel
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideLoggingInterceptor() }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideApi(get()) }
    single { provideRetrofit(get()) }
}

val viewModelModule = module {
    viewModel {
        UserSearchViewModel(usersListDataSourceFactory = get())
    }

    viewModel {
        UserInfoViewModel(apiRepository = get())
    }
}

val userListSourceFactory = module {
    factory { UsersListDataSourceFactory(apiRepository = get()) }
}

val repositoryModule = module {
    factory { UserRepository(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(Constant.HOST_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}


fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        //Github api Basic Authentication　
        request.addHeader("Content-Type", "application/json;charset=UTF-8")
            .addHeader(
                "Authorization",
                Credentials.basic(Constant.GITHUB_USER, Constant.GITHUB_API_TOKEN)
            )
        return chain.proceed(request.build())
    }

}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG)
        logger.level = HttpLoggingInterceptor.Level.BASIC
    else
        logger.level = HttpLoggingInterceptor.Level.NONE
    return logger
}

fun provideApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)