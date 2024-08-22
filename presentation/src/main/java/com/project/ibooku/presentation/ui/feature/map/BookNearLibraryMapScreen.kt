package com.project.ibooku.presentation.ui.feature.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.theme.IbookuTheme


@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun BookNearLibraryMapScreen(navController: NavHostController){
    StatusBarColorsTheme()

    IbookuTheme{
        Scaffold { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                    NaverMap(modifier = Modifier.fillMaxSize())
            }
        }
    }
}