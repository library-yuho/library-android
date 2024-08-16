package com.project.ibooku.presentation.ui.screens.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.project.ibooku.data.remote.UrlLink.CENTRAL_HOMEPAGE_URL
import com.project.ibooku.domain.model.PopularBooksModel
import com.project.ibooku.presentation.R
import com.project.ibooku.presentation.ui.screens.search.ActivityBookSearch
import com.project.ibooku.presentation.ui.theme.Gray10
import com.project.ibooku.presentation.ui.theme.Gray20
import com.project.ibooku.presentation.ui.theme.Gray30
import com.project.ibooku.presentation.ui.theme.Gray40
import com.project.ibooku.presentation.ui.theme.Gray50
import com.project.ibooku.presentation.ui.theme.IbookuTheme
import com.project.ibooku.presentation.ui.theme.PlaceHolderColor
import com.project.ibooku.presentation.ui.theme.SkyBlue10
import com.project.ibooku.presentation.ui.theme.White
import com.project.ibooku.presentation.ui.theme.notosanskr
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityHome : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            BarColorsTheme()

            IbookuTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
                    NavigationGraph(navController = navController, innerPadding = innerPadding)
                }
            }
        }
    }
}

@Composable
fun BarColorsTheme(darkTheme: Boolean = isSystemInDarkTheme()) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SkyBlue10.toArgb()

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = darkTheme
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = darkTheme
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Menu
    )

    NavigationBar(
        containerColor = White,
        contentColor = Gray20
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                icon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.titleId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleId),
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
                    unselectedIconColor = Gray20,
                    selectedTextColor = SkyBlue10,
                    unselectedTextColor = Gray20,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(modifier = modifier.padding(innerPadding))
        }
        composable(BottomNavItem.Menu.route) {

        }
    }
}


/**
 * 홈 화면
 * @param modifier
 */
@Composable
@Preview(showBackground = true, heightDp = 3000)
private fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val isLoading = viewModel.homeState.isLoading

    val scrollState = rememberScrollState()
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            HomeHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            val popularBookList = viewModel.homeState.popularBooks
            HomeBody(
                popularBookList = popularBookList,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun HomeHeader(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(SkyBlue10)
            .padding(15.dp)
    ) {
        HomeHeaderTop(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(15.dp))

        HomeHeaderSearch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun HomeHeaderTop(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = White,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(id = R.string.home_header_text),
                color = White,
                fontSize = 12.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr
            )
        }

        Row(modifier = Modifier.wrapContentSize()) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = Icons.Default.Notifications,
                    tint = White,
                    contentDescription = null
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = Icons.Default.Person,
                    tint = White,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun HomeHeaderSearch(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Surface(modifier = modifier, shape = RoundedCornerShape(100),
        onClick = {
            context.startActivity(
                Intent(context, ActivityBookSearch::class.java)
            )
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
                .padding(start = 26.dp, end = 10.dp, top = 6.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.home_header_search_place_holder),
                color = PlaceHolderColor,
                fontSize = 14.sp,
                fontFamily = notosanskr,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            )

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Search,
                    tint = SkyBlue10,
                    contentDescription = null
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
private fun HomeBody(popularBookList: List<PopularBooksModel>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Surface(modifier = modifier.background(Color.Transparent), shape = RoundedCornerShape(16.dp)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray10)
        ) {
            HomeBodyReview(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(26.dp))

            HomeBodyPopularBook(
                popularBookList = popularBookList,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeBodyHelp(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                object : HomeHelpClickListener {
                    override fun onUserGuideClick() {

                    }

                    override fun onHomePageClick() {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(CENTRAL_HOMEPAGE_URL))
                        context.startActivity(intent)
                    }

                    override fun onCsClick() {

                    }

                }
            )
        }
    }
}

@Composable
private fun HomeBodyReview(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 18.dp, end = 18.dp, top = 28.dp, bottom = 16.dp)) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = stringResource(id = R.string.home_body_review),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        HomeBodyReviewWrite(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HomeBodyReviewRead(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun HomeBodyReviewWrite(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.background(White),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 4.dp,
        onClick = { /*TODO*/ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
                .padding(start = 22.dp, end = 6.dp, top = 16.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()

            ) {
                Text(
                    text = stringResource(id = R.string.home_body_review_write_desc),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray20
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.home_body_review_write_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray40
                )
            }

            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = Gray30,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun HomeBodyReviewRead(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.background(White),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 4.dp,
        onClick = { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
                .padding(start = 14.dp, end = 6.dp, top = 16.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    val randInt = (0..2).random()
                    val descState by remember {
                        mutableIntStateOf(randInt)
                    }
                    val descStrId = when (descState) {
                        0 -> R.string.home_body_review_read_desc1
                        1 -> R.string.home_body_review_read_desc2
                        else -> R.string.home_body_review_read_desc3
                    }

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(id = descStrId),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray20
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(id = R.string.home_body_review_read_title),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray40
                    )
                }

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    tint = Gray30,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(), painter = painterResource(
                    id = R.drawable.img_home_map_example
                ), contentDescription = null
            )
        }
    }
}

@Composable
private fun HomeBodyPopularBook(
    popularBookList: List<PopularBooksModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.home_body_popular_book),
                    color = Gray50,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                /*TODO: 날짜 바인딩 */
                Text(
                    text = "(24.07.24 ~ 24.07.30)",
                    color = Gray20,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            TextButton(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Gray10),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    modifier = Modifier.background(Gray10),
                    text = stringResource(id = R.string.home_body_popular_book_see_details),
                    color = SkyBlue10,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 6.dp, start = 12.dp, end = 12.dp)
        ) {
            items(items = popularBookList) { item ->
                Surface(
                    modifier = Modifier
                        .width(240.dp)
                        .wrapContentHeight()
                        .padding(6.dp)
                        .border(width = 0.5.dp, color = Gray10, RoundedCornerShape(10.dp)),
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .background(White)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.ranking,
                            color = Gray40,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        AsyncImage(
                            modifier = Modifier.size(50.dp, 74.dp),
                            model = item.bookImgUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = item.bookName,
                                color = Gray40,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                            Text(
                                text = item.authors,
                                color = Gray20,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun HomeBodyHelp(modifier: Modifier = Modifier, helpClickListener: HomeHelpClickListener) {
    Column(modifier = modifier.padding(14.dp)) {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(6.dp),
            text = stringResource(id = R.string.home_body_help),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Gray50
        )

        Spacer(modifier = Modifier.height(10.dp))

        HomeBodyHelpUserGuide(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) { helpClickListener.onUserGuideClick() }

        Spacer(modifier = Modifier.height(14.dp))

        HomeBodyHelpHomepageAndCs(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            { helpClickListener.onHomePageClick() },
            { helpClickListener.onCsClick() }
        )

    }
}

@Composable
private fun HomeBodyHelpUserGuide(modifier: Modifier = Modifier, onUserGuideClick: () -> Unit) {
    Surface(
        modifier = modifier
            .background(White),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 4.dp,
        onClick = onUserGuideClick
    ) {
        Row(modifier = Modifier.background(White)) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(White)
                    .padding(20.dp)
            ) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(
                        id = R.drawable.img_home_help_profile
                    ),
                    contentDescription = null
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.home_body_help_guide_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray40
                    )

                    Text(
                        text = stringResource(id = R.string.home_body_help_guide_desc),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray20
                    )
                }

            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Top) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Close,
                        tint = Gray20,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeBodyHelpHomepageAndCs(
    modifier: Modifier = Modifier,
    onHomePageClick: () -> Unit,
    onCsClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .background(White),
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(10.dp),
            onClick = onHomePageClick
        ) {
            Row(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 15.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(48.dp),
                    painter = painterResource(
                        id = R.drawable.img_home_homepage
                    ),
                    contentDescription = null
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.home_body_help_homepage_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Gray40
                    )

                    Text(
                        text = stringResource(id = R.string.home_body_help_homepage_desc),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray20
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Surface(
            modifier = Modifier
                .weight(1f)
                .background(White),
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(10.dp),
            onClick = onCsClick
        ) {
            Row(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 15.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(48.dp),
                    painter = painterResource(
                        id = R.drawable.img_home_cs
                    ),
                    contentDescription = null
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.home_body_help_cs_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Gray40
                    )

                    Text(
                        text = stringResource(id = R.string.home_body_help_cs_desc),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray20
                    )
                }
            }
        }
    }
}


sealed class BottomNavItem(val titleId: Int, val route: String, val icon: ImageVector) {
    object Home : BottomNavItem(R.string.nav_home, "home", Icons.Default.Home)
    object Menu : BottomNavItem(R.string.nav_menu, "menu", Icons.Default.Menu)
}

interface HomeHelpClickListener {
    fun onUserGuideClick()
    fun onHomePageClick()
    fun onCsClick()
}
