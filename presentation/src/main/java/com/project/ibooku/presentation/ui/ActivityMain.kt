package com.project.ibooku.presentation.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.ibooku.domain.model.book.BookInfoModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.feature.auth.AuthViewModel
import com.project.ibooku.presentation.ui.feature.auth.screen.login.InputEmailScreen
import com.project.ibooku.presentation.ui.feature.auth.screen.login.InputPasswordScreen
import com.project.ibooku.presentation.ui.feature.auth.screen.signup.SignUpAuthScreen
import com.project.ibooku.presentation.ui.feature.auth.screen.signup.SignUpInputInfoScreen
import com.project.ibooku.presentation.ui.feature.auth.screen.signup.SignUpInputPasswordScreen
import com.project.ibooku.presentation.ui.feature.book.BookDetailEvents
import com.project.ibooku.presentation.ui.feature.book.BookDetailScreen
import com.project.ibooku.presentation.ui.feature.book.BookDetailViewModel
import com.project.ibooku.presentation.ui.feature.home.HomeScreen
import com.project.ibooku.presentation.ui.feature.library.BookNearLibraryMapScreen
import com.project.ibooku.presentation.ui.feature.library.LibraryEvents
import com.project.ibooku.presentation.ui.feature.library.LibraryViewModel
import com.project.ibooku.presentation.ui.feature.review.BookReviewEvents
import com.project.ibooku.presentation.ui.feature.review.screen.BookReviewLocationMapScreen
import com.project.ibooku.presentation.ui.feature.review.screen.BookReviewReadMap
import com.project.ibooku.presentation.ui.feature.review.BookReviewViewModel
import com.project.ibooku.presentation.ui.feature.review.screen.BookReviewCompleteScreen
import com.project.ibooku.presentation.ui.feature.review.screen.BookReviewLocationScreen
import com.project.ibooku.presentation.ui.feature.review.screen.BookReviewOnboardingScreen
import com.project.ibooku.presentation.ui.feature.review.screen.BookReviewWriteScreen
import com.project.ibooku.presentation.ui.feature.review.screen.BookSearchScreenAtReview
import com.project.ibooku.presentation.ui.feature.search.BookInfoViewModel
import com.project.ibooku.presentation.ui.feature.search.BookInfoEvents
import com.project.ibooku.presentation.ui.feature.search.BookSearchScreen
import com.project.ibooku.presentation.ui.feature.splash.SplashScreen
import com.project.ibooku.presentation.ui.feature.splash.SplashViewModel
import com.project.ibooku.presentation.ui.theme.Black
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityHome : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun StatusBarColorsTheme(
    statusBarColor: Color? = null,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                statusBarColor?.toArgb() ?: if (isDarkTheme) Black.toArgb() else White.toArgb()

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = if (statusBarColor != null) false else !isDarkTheme
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        NavItem.Home,
        NavItem.Menu
    )

    NavigationBar(
        containerColor = White,
        contentColor = Gray50
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            val (titleId, icon) = when (item.route) {
                "home" -> Pair(R.string.nav_home, Icons.Default.Home)
                "menu" -> Pair(R.string.nav_menu, Icons.Default.Menu)
                else -> Pair(null, Icons.Default.Close)
            }
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                icon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = icon,
                        contentDescription = titleId?.let { stringResource(id = it) }
                    )
                },
                label = {
                    Text(
                        text = titleId?.let { stringResource(id = it) } ?: "",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SkyBlue10,
                    unselectedIconColor = Gray50,
                    selectedTextColor = SkyBlue10,
                    unselectedTextColor = Gray50,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


@Composable
private fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavItem.Splash.route,
        modifier = modifier
    ) {
        composable(NavItem.Splash.route) {
            SplashScreen(
                navController = navController,
                viewModel = hiltViewModel<SplashViewModel>()
            )
        }

        composable(NavItem.InputEmail.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.InputEmail.route)
            }
            InputEmailScreen(
                navController = navController,
                viewModel = hiltViewModel<AuthViewModel>(prevEntry)
            )
        }

        composable(NavItem.InputPassword.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.InputEmail.route)
            }
            InputPasswordScreen(
                navController = navController,
                viewModel = hiltViewModel<AuthViewModel>(prevEntry)
            )
        }

        composable(NavItem.SignUpAuth.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.InputEmail.route)
            }
            SignUpAuthScreen(
                navController = navController,
                viewModel = hiltViewModel<AuthViewModel>(prevEntry)
            )
        }

        composable(NavItem.SignUpInputPassword.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.InputEmail.route)
            }
            SignUpInputPasswordScreen(
                navController = navController,
                viewModel = hiltViewModel<AuthViewModel>(prevEntry)
            )
        }

        composable(NavItem.SignUpInputInfo.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.InputEmail.route)
            }
            SignUpInputInfoScreen(
                navController = navController,
                viewModel = hiltViewModel<AuthViewModel>(prevEntry)
            )
        }

        composable(NavItem.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(NavItem.Menu.route) {

        }
        composable(NavItem.BookSearch.route) { backStackEntry ->
            val prevRoute = navController.previousBackStackEntry?.destination?.route
            // 리뷰 온보딩에서 진입한 경우에는 리뷰용 검색 화면에 진입하도록 함.
            if (prevRoute == NavItem.BookReviewOnboarding.route) {
                val prevEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavItem.BookReviewOnboarding.route)
                }
                BookSearchScreenAtReview(
                    navController = navController,
                    viewModel = hiltViewModel<BookReviewViewModel>(prevEntry)
                )
            } else {
                BookSearchScreen(navController = navController)
            }
        }
        composable(
            NavItem.BookDetail.route,
            arguments = listOf(navArgument("isbn") { type = NavType.StringType })
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            if (isbn != "{isbn}") {
                val bookInfoViewModel = hiltViewModel<BookDetailViewModel>()
                bookInfoViewModel.onEvent(BookDetailEvents.BookSelected(isbn = isbn))

                BookDetailScreen(
                    navController = navController,
                    viewModel = bookInfoViewModel
                )
            }
        }

        composable(
            NavItem.BookNearLibraryMap.route,
            arguments = listOf(
                navArgument("isbn") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType })
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""

            val libraryViewModel = hiltViewModel<LibraryViewModel>()
            libraryViewModel.onEvent(
                LibraryEvents.OnBookSelected(
                    isbn = isbn,
                    title = title,
                    author = author
                )
            )

            BookNearLibraryMapScreen(navController = navController, viewModel = libraryViewModel)
        }
        composable(NavItem.BookReviewReadMap.route) {
            BookReviewReadMap(navController = navController)
        }
        composable(NavItem.BookReviewOnboarding.route) {
            BookReviewOnboardingScreen(
                navController = navController,
                viewModel = hiltViewModel<BookReviewViewModel>()
            )
        }
        composable(NavItem.BookReviewWrite.route,
            arguments = listOf(
                navArgument("isbn") { type = NavType.StringType })) { backStackEntry ->

            val isbn = backStackEntry.arguments?.getString("isbn") ?: ""
            if (isbn != "{isbn}") {
                val bookInfoViewModel = hiltViewModel<BookReviewViewModel>()
                bookInfoViewModel.onEvent(BookReviewEvents.SearchResultItemsSelected(isbn = isbn))

                BookReviewWriteScreen(
                    navController = navController,
                    viewModel = bookInfoViewModel
                )
            }else{
                val prevEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavItem.BookReviewOnboarding.route)
                }
                BookReviewWriteScreen(
                    navController = navController,
                    viewModel = hiltViewModel<BookReviewViewModel>(prevEntry)
                )
            }
        }
        composable(NavItem.BookReviewLocation.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.BookReviewWrite.route)
            }
            BookReviewLocationScreen(
                navController = navController,
                viewModel = hiltViewModel<BookReviewViewModel>(prevEntry)
            )
        }
        composable(NavItem.BookReviewLocationMap.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.BookReviewWrite.route)
            }
            BookReviewLocationMapScreen(
                navController = navController,
                viewModel = hiltViewModel<BookReviewViewModel>(prevEntry)
            )
        }
        composable(NavItem.BookReviewComplete.route) { backStackEntry ->
            val prevEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavItem.BookReviewWrite.route)
            }
            BookReviewCompleteScreen(
                navController = navController,
                viewModel = hiltViewModel<BookReviewViewModel>(prevEntry)
            )
        }
    }
}


sealed class NavItem(val route: String) {
    data object Splash : NavItem("splash")
    data object InputEmail : NavItem("input_email")
    data object InputPassword : NavItem("input_password")
    data object SignUpAuth : NavItem("sign_up_auth")
    data object SignUpInputPassword : NavItem("sign_up_input_password")
    data object SignUpInputInfo : NavItem("sign_up_input_info")
    data object Home : NavItem("home")
    data object Menu : NavItem("menu")
    data object BookSearch : NavItem("book_search")
    data object BookDetail : NavItem("book_detail/{isbn}")
    data object BookNearLibraryMap : NavItem("book_near_library_map/{isbn}/{title}/{author}")
    data object BookReviewReadMap : NavItem("book_review_read_map")
    data object BookReviewOnboarding : NavItem("book_review_onboarding")
    data object BookReviewWrite : NavItem("book_review_write/{isbn}")
    data object BookReviewLocation : NavItem("book_review_location")
    data object BookReviewLocationMap : NavItem("book_review_location_map")
    data object BookReviewComplete : NavItem("book_review_complete")
}

