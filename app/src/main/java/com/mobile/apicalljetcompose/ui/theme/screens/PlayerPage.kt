package com.mobile.apicalljetcompose.ui.theme.screens

import PlayerViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.PlayerBgDown
import com.mobile.apicalljetcompose.ui.theme.PlayerBgDownLight
import com.mobile.apicalljetcompose.ui.theme.PlayerBgUp
import com.mobile.apicalljetcompose.ui.theme.PlayerBgUpLight
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(isDark: Boolean = false, onChangeTheme: () -> Unit = {}, playerViewModel: PlayerViewModel, songViewModel: SongViewModel = viewModel(), navController: NavController){


    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    val menuItems = listOf("Play Next", "Add To Playlist", "Info Tags", "Edit Tags", "Set As Ringtone", "Exit")

    var selectedItem by remember { mutableStateOf(menuItems[0]) }


    val isPlaying by playerViewModel.isPlaying.collectAsState()
    val songId by playerViewModel.songId.collectAsState()
    val songUri by playerViewModel.songUri.collectAsState()
    val currentSongTitle by playerViewModel.currentSong.collectAsState()
    val currentSongArtist by playerViewModel.currentArtist.collectAsState()
    val currentPosition by playerViewModel.currentPosition.collectAsState()
    val totalDuration by playerViewModel.totalDuration.collectAsState()
    val isShuffle by playerViewModel.isShuffle.collectAsState()




    val isFav = playerViewModel.isFav.collectAsState()

    var sliderPosition by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()


    val systemUiController = rememberSystemUiController()

    // Set status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }

    LaunchedEffect(currentPosition) {
        sliderPosition = if (totalDuration > 0) currentPosition.toFloat() / totalDuration.toFloat() else 0f
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(brush = Brush.linearGradient(listOf(if (isDark) PlayerBgUp else PlayerBgUpLight, if (isDark) PlayerBgDown else PlayerBgDownLight)))){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){


            IconButton(onClick = {
                navController.popBackStack()
            },
                ) {
                Icon(painter = painterResource(R.drawable.baseline_arrow_back_24), contentDescription = null)
            }



        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(347.dp)
            .padding(20.dp)
            .background(Color.Black, shape = RoundedCornerShape(26.dp)),
            contentAlignment = Alignment.Center
        ){

            Image(
                painter = painterResource(id = R.drawable.player_bg_img
                ),
                contentDescription = "more",
                modifier = Modifier
                    .width(350.dp)
                    .height(347.dp)
                    .clickable {

                    }
            )
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),

                ){
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    Box(modifier = Modifier
                        .size(41.dp)
                        .background(color = Color(0xffD9D9D9).copy(0.4f), shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu
                            ),
                            contentDescription = "more",
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp)
                                .clickable {
                                    expanded = !expanded
                                }
                        )
                    }
                }




                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,

                    ) {
                    Box {


                        DropdownMenu(
                            modifier = Modifier
                                .width(154.dp)
                                .height(227.dp),
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            menuItems.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        selectedItem = item
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }








                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        0.dp, 0.dp, 0.dp, 10.dp,
                    ),
                    contentAlignment = Alignment.BottomStart){
                    Row (modifier = Modifier.fillMaxWidth(),){
                        Box(modifier = Modifier
                            .width(46.dp)
                            .height(29.dp)
                            .background(
                                color = Color(0xff373333).copy(0.6f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_like
                                ),
                                contentDescription = "more",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                                    .align(Alignment.Center)
                                    .clickable {

                                    }
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(modifier = Modifier
                            .width(46.dp)
                            .height(29.dp)
                            .background(
                                color = Color(0xff373333).copy(0.6f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_dislike
                                ),
                                contentDescription = "more",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                                    .align(Alignment.BottomCenter)
                                    .clickable {

                                    }
                            )
                        }

                    }
                }
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ){



            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${currentSongTitle}",
                    fontFamily = jost,
                    fontSize = 14.sp,
                    color = if (isDark) Color.White.copy(0.8f) else Color.Black,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "${currentSongArtist}",
                    fontFamily = jost,
                    fontSize = 12.sp,
                    color = if (isDark) Color.White.copy(0.8f) else Color.Black,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center,
                )
//                Text(
//                    text = "All songs (8/200)",
//                    fontFamily = jost,
//                    fontSize = 12.sp,
//                    color = Color.White.copy(0.8f),
//                    fontWeight = FontWeight.W500,
//                    textAlign = TextAlign.Center,
//                )
            }




        }


        Box(modifier = Modifier.fillMaxSize()){
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){

                Column (verticalArrangement = Arrangement.Center){
                    Slider(
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            activeTrackColor = Color.Black,
                            inactiveTrackColor = Color.Gray,
                            activeTickColor = Color.Black,
                            thumbColor = Color.Black,
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(20.dp),
                        value = sliderPosition,
                        onValueChange = { newPosition -> sliderPosition = newPosition },
                        onValueChangeFinished = {
                            scope.launch {
                                val seekToPosition = (sliderPosition * totalDuration).toLong()
                                playerViewModel.seekTo(seekToPosition)
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    Box(modifier = Modifier.fillMaxWidth()){
                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){



                            Box(modifier = Modifier
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .padding(7.dp)){
                                Text(text = "${playerViewModel.formatDuration(currentPosition)}",
                                    color = Color.White,
                                    fontFamily = jost,
                                    )
                            }


                            Box(modifier = Modifier
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .padding(7.dp)){
                                Text(text = "${playerViewModel.formatDuration(totalDuration)}",
                                    color = Color.White,
                                    fontFamily = jost,
                                    )
                            }



                        }
                    }

                }



                Row (modifier= Modifier
                    .width(276.dp)
                    .height(76.dp)
                    .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ){

                    Box(modifier = Modifier
                        .size(38.dp)
                        .background(color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ){


                        if (isFav.value){
                            Image(
                                colorFilter = ColorFilter.tint(color = if (isDark) AppThemeColor else Color.Red),
                                painter = painterResource(id = R.drawable.baseline_favorite_24),
                                contentDescription = "more",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                                    .clickable {
                                        scope.launch {
                                            playerViewModel.database.removeSongFromFav(songId)
                                        }
                                    }
                            )
                        }else{
                            Image(
                                colorFilter = ColorFilter.tint(color = if (isDark) AppThemeColor else Color.White),
                                painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                                contentDescription = "more",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                                    .clickable {
                                        scope.launch {
                                            songViewModel.addToFavorite(Favorite(0,songId.toString(),currentSongTitle,songUri,currentSongArtist,totalDuration), songId)
                                        }
                                    }
                            )
                        }



                    }

                    Box(modifier = Modifier
                        .size(54.dp)
                        .background(color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ){

                        IconButton(onClick = {
                            playerViewModel.previous()
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_skip_previous_24), tint = if (isDark) Color.White else Color.White,contentDescription = null,)
                        }

                    }

                    Box(modifier = Modifier
                        .size(76.dp)
                        .background(color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ){
                        Image(
                            painter = painterResource(id = if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                            contentDescription = "more",
                            modifier = Modifier
                                .width(61.dp)
                                .height(61.dp)
                                .clickable {
                                    playerViewModel.resume()
                                }
                        )
                    }

                    Box(modifier = Modifier
                        .size(54.dp)
                        .background(color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ){

                        IconButton(onClick = {
                            playerViewModel.next()
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_skip_next_24), tint = if (isDark) Color.White else Color.White, contentDescription = null)
                        }
                    }

                    Box(modifier = Modifier
                        .size(38.dp)
                        .background(color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ){
                        Image(
                            colorFilter = if (isShuffle) ColorFilter.tint(color = if (isDark) AppThemeColor else Color.Red) else ColorFilter.tint(color = Color.White),
                            painter = painterResource(id = R.drawable.ic_shuffle),
                            contentDescription = "more",
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .clickable {
                                    scope.launch {
                                        playerViewModel.shuffle()
                                    }
                                }
                        )
                    }


                }



            }
        }








    }
}


@Preview(showBackground = true)
@Composable
fun PlayerpagePreview(){

    val navController = rememberNavController()
    val playerViewModel : PlayerViewModel = viewModel()
    PlayerScreen(playerViewModel = playerViewModel,navController = navController)
}


