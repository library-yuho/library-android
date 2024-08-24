package com.project.ibooku.data.di

import com.project.ibooku.data.BuildConfig
import com.project.ibooku.data.remote.Domain
import com.project.ibooku.data.remote.service.external.CentralService
import com.project.ibooku.data.remote.service.external.NaruService
import com.project.ibooku.data.remote.service.general.BookService
import com.project.ibooku.data.remote.service.general.ReviewService
import com.project.ibooku.data.remote.service.general.UserService
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getMatchedHttpClient(apiKey: String? = null): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")

                val httpUrl = chain.request()
                    .url
                    .newBuilder().apply {
                        when (apiKey) {
                            BuildConfig.NARU_API_KEY -> {
                                addQueryParameter("authKey", apiKey)
                                addQueryParameter("format", "json")
                            }
                            BuildConfig.CENTRAL_API_KEY -> {
                                addQueryParameter("key", apiKey)
                                addQueryParameter("apiType", "json")
                            }
                        }
                    }.build()

                val request = requestBuilder
                    .url(httpUrl)
                    .build()

                chain.proceed(request)
            }
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideNaruService(): NaruService {
        val naruRetrofit = Retrofit.Builder()
            .baseUrl(Domain.INFORMATION_NARU)
            .client(getMatchedHttpClient(BuildConfig.NARU_API_KEY))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return naruRetrofit.create(NaruService::class.java)
    }

    @Provides
    @Singleton
    fun provideCentralService(): CentralService {
        val centralRetrofit = Retrofit.Builder()
            .baseUrl(Domain.CENTRAL)
            .client(getMatchedHttpClient(BuildConfig.CENTRAL_API_KEY))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return centralRetrofit.create(CentralService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(): UserService {
        val generalRetrofit = Retrofit.Builder()
            .baseUrl(Domain.GENERAL)
            .client(getMatchedHttpClient())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return generalRetrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewService(): ReviewService {
        val generalRetrofit = Retrofit.Builder()
            .baseUrl(Domain.GENERAL)
            .client(getMatchedHttpClient())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return generalRetrofit.create(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookService(): BookService {
        val generalRetrofit = Retrofit.Builder()
            .baseUrl(Domain.GENERAL)
            .client(getMatchedHttpClient())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return generalRetrofit.create(BookService::class.java)
    }
}