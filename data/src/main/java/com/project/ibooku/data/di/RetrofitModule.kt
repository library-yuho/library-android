package com.project.ibooku.data.di

import com.project.ibooku.data.BuildConfig
import com.project.ibooku.data.remote.Domain
import com.project.ibooku.data.remote.service.CentralService
import com.project.ibooku.data.remote.service.NaruService
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
object RetrofitModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getMatchedHttpClient(apiKey: String?): OkHttpClient =
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
    fun provideCentralService(): CentralService{
        val centralRetrofit = Retrofit.Builder()
            .baseUrl(Domain.CENTRAL)
            .client(getMatchedHttpClient(BuildConfig.CENTRAL_API_KEY))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return centralRetrofit.create(CentralService::class.java)
    }

}