package com.project.ibooku.presentation.ui.feature.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.theme.IbookuTheme

@Composable
fun BookDetailScreen(navController: NavController){
    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

            }
        }
    }
}