package com.mobile.apicalljetcompose.ui.theme.screens

import PlayerViewModel
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.BottomPlayerColor
import com.mobile.apicalljetcompose.ui.theme.BoxBackground
import com.mobile.apicalljetcompose.ui.theme.TabBarTextColor
import com.mobile.apicalljetcompose.ui.theme.ThemeAndColorTheme
import com.mobile.apicalljetcompose.ui.theme.constants.AppConstants
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.thememanager.ThemePreferenceManager
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabScreen(isDark: Boolean, onChangeTheme: () -> Unit,playerViewModel: PlayerViewModel, songViewModel: SongViewModel,navController: NavController,themePreferenceManager: ThemePreferenceManager) {


    val tabTitles = listOf("Playlists", "Songs","Favorite")
    val pagerState = rememberPagerState(pageCount = {tabTitles.size})
    val coroutineScope = rememberCoroutineScope()




    val systemUiController = rememberSystemUiController()

    var dialog by remember { mutableStateOf(false) }

    // Set status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }


    BackHandler {
        dialog = true
    }



    Box (modifier = Modifier.fillMaxSize()){

        if (dialog){
            AlertDialog(
                onDismissRequest = { /* Prevent dismissal */ },
                title = {
                    androidx.compose.material.Text(
                        text = "Are you sure exit the app ?",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W600,
                        color = if (isDark) Color.White else Color.Black,
                        fontFamily = jost
                    )
                },
                text = {
                },
                dismissButton = {
                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        },
                        text = "yes",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W400,
                        color = if (isDark) AppThemeColor else Color.Red,
                        fontFamily = jost
                    )
                },
                confirmButton = {
                    androidx.compose.material.Text(
                        modifier = Modifier.clickable {
                            dialog = !dialog
                        },
                        text = "no",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W400,
                        color = if (isDark) AppThemeColor else Color.Red,
                        fontFamily = jost
                    )
                }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {



            Row (modifier = Modifier
                .fillMaxWidth()
            ){

                TabRow(
                    divider = {},
                    selectedTabIndex = pagerState.currentPage,
                    indicator = {tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            height = 3.dp,
                            color = if (isDark) AppThemeColor else Color.Red
                        )
                    },
                ) {

                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            unselectedContentColor = if (isDark) Color.White else TabBarTextColor,
                            selectedContentColor = if (isDark) AppThemeColor else  Color.Red,
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(title,
                                fontFamily = jost,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W700,
                            )
                            }
                        )
                    }
                }




            }






            // Tab Content (Pager)
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> PlaylistScreen(isDark,onChangeTheme, navController = navController, themePreferenceManager = themePreferenceManager)
                    1 -> SongsScreen(isDark,onChangeTheme, playerViewModel = playerViewModel,songViewModel = songViewModel, navController = navController)
                    2 -> FavoriteScreen(isDark,onChangeTheme, playerViewModel = playerViewModel,navController = navController)
                }




            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)){
            BottomMiniPlayer(isDark = isDark,onChangeTheme, playerViewModel,songViewModel,navController)
        }


    }
}


@Composable
fun BottomMiniPlayer(isDark: Boolean,onChangeTheme: () -> Unit,playerViewModel: PlayerViewModel,songViewModel: SongViewModel,navController: NavController){

    val isPlaying by playerViewModel.isPlaying.collectAsState()
    val currentSongTitle by playerViewModel.currentSong.collectAsState()
    val scope = rememberCoroutineScope()


    val songId by playerViewModel.songId.collectAsState()
    val songUri by playerViewModel.songUri.collectAsState()
    val currentSongArtist by playerViewModel.currentArtist.collectAsState()
    val currentPosition by playerViewModel.currentPosition.collectAsState()
    val totalDuration by playerViewModel.totalDuration.collectAsState()
    val isShuffle by playerViewModel.isShuffle.collectAsState()




    val isFav = playerViewModel.isFav.collectAsState()

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clickable {
            navController.navigate(AppConstants.PLAYER_SCREEN_ROUTE)
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) BoxBackground else TabBarTextColor,
        ),
        shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
    ){
        Box(modifier = Modifier.fillMaxSize()){
            Column (modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(10.dp, end = 10.dp, bottom = 0.dp, top = 10.dp),
                verticalArrangement = Arrangement.Top,
            ){



                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){

                    Column{

                        Text(
                            modifier = Modifier.fillMaxWidth(0.4f),
                            text = "${currentSongTitle}",
                            fontFamily = jost,
                            fontSize = 14.sp,
                            minLines = 1,
                            maxLines = 1,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Center,
                        )

                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){

                        if (isFav.value){
                            IconButton(onClick = {
                                scope.launch {
                                    playerViewModel.database.removeSongFromFav(songId)
                                }
                            }) {

                                Image(
                                    colorFilter = ColorFilter.tint(color =  if (isDark) Color.White else Color.Black),
                                    painter = painterResource(id = R.drawable.baseline_favorite_24),
                                    contentDescription = "more",
                                )

                            }
                        }else{
                            IconButton(onClick = {
                                scope.launch {
                                    songViewModel.addToFavorite(Favorite(0,songId.toString(),currentSongTitle,songUri,currentSongArtist,totalDuration), songId)
                                }
                            }) {
                                Image(
                                    colorFilter = ColorFilter.tint(color = if (isDark) Color.White else Color.Black),
                                    painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                                    contentDescription = "more",
                                )
                            }
                        }

                        IconButton(onClick = { playerViewModel.previous() }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = "ic_next")
                        }

                        if (isPlaying){
                            IconButton(onClick = { playerViewModel.resume() }) {
                                Icon(painter = painterResource(id = R.drawable.baseline_pause_24), contentDescription = "ic_pause")
                            }
                        }else{
                            IconButton(onClick = { playerViewModel.resume() }) {
                                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = "ic_pause")
                            }
                        }


                        IconButton(onClick = { playerViewModel.next() }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = "ic_next")
                        }


                    }
                }

            }
        }

    }
}


@Composable
fun NotifyBanner(message : String,isDark: Boolean){
    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        .border(border = BorderStroke(width = 1.dp, color = Color.White.copy(0.4f)), shape = RoundedCornerShape(15.dp))
        .background(color = Color.Gray.copy(alpha = 0.2f))){
        Text(
            modifier = Modifier.padding(5.dp),
            text = "$message",
            fontFamily = jost,
            fontSize = 13.sp,
            color = if(isDark) Color.White else Color.Black,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
        )
    }
}







