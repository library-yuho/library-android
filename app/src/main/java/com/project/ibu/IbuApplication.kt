package com.project.ibu

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IbuApplication: Application(){
    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIENT_API_KEY_ID)
    }
}