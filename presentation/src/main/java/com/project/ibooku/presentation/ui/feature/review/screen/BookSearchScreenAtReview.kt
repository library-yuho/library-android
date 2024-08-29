package com.project.ibooku.presentation.ui.feature.review.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.project.ibooku.presentation.ui.NavItem
import com.project.ibooku.presentation.ui.StatusBarColorsTheme
import com.project.ibooku.presentation.ui.feature.review.BookReviewEvents
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.feature.search.BookSearchCommonScreen
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.White

@Composable
fun BookSearchScreenAtReview(
    navController: NavController,
    viewModel: BookReviewViewModel = hiltViewModel()
) {
    StatusBarColorsTheme()

    IbookuTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            val bookReviewState = viewModel.state.collectAsStateWithLifecycle()
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current

            BackHandler {
                if (bookReviewState.value.searchKeyword.isEmpty()) {
                    navController.popBackStack()
                } else {
                    viewModel.onEvent(BookReviewEvents.SearchTextChanged(""))
                }
            }

            BookSearchCommonScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(it),
                relatedKeywordList = bookReviewState.value.relatedKeywordList,
                searchKeyword = bookReviewState.value.searchKeyword,
                searchResult = bookReviewState.value.searchResult,
                isSearchLoading = bookReviewState.value.isSearchLoading,
                onBackPressed = {
                    navController.popBackStack()
                },
                onSearch = {
                    if (bookReviewState.value.searchKeyword.isNotEmpty()) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                    viewModel.onEvent(BookReviewEvents.SearchKeyword)
                },
                onChipSelected = { keyword ->
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.onEvent(
                        BookReviewEvents.SearchWithSelectionSomething(keyword)
                    )
                },
                onTextChanged = { keyword ->
                    viewModel.onEvent(BookReviewEvents.SearchTextChanged(keyword))
                },
                onRelatedKeywordClick = { relatedKeyword ->
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    viewModel.onEvent(
                        BookReviewEvents.SearchWithSelectionSomething(
                            relatedKeyword
                        )
                    )
                },
                onResultItemClick = { result ->
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    if(result.isbn.isNotEmpty()){
                        viewModel.onEvent(BookReviewEvents.SearchResultItemsSelected(result.isbn))
                        navController.navigate(NavItem.BookReviewWrite.route)
                    }
                }
            )
        }
    }
}