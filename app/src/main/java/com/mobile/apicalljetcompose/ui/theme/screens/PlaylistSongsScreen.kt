package com.mobile.apicalljetcompose.ui.theme.screens

import PlayerViewModel
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.BoxBackground
import com.mobile.apicalljetcompose.ui.theme.TabBarTextColor
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.PlaylistSongs
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.viewmodel.FavoriteViewModel
import com.mobile.apicalljetcompose.ui.theme.viewmodel.PlaylistViewModel
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel
import kotlinx.coroutines.launch


@Composable
fun PlaylistSongsScreen(
    isDark: Boolean,
    onChangeTheme: () -> Unit,
    playlistId: Long,
    playlistName: String,
    playerViewModel: PlayerViewModel,
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel = viewModel(),
    navController: NavController
) {

    val playlistSongsList by playlistViewModel.playlistSongsList.observeAsState(emptyList())

    val forSetSongsAsPlayer = remember { mutableStateListOf<SongData>() }

    val couroutineScope = rememberCoroutineScope()


    val systemUiController = rememberSystemUiController()


    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }

    LaunchedEffect(playlistId) {
        Log.d("Screen","getRequest")
        playlistViewModel.getPlaylistSongs(playlistId)
        Log.d("Screen","get sucess  ${playlistId} ${playlistSongsList}")
    }

    Box(modifier = Modifier.fillMaxSize()) {


        Column(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier.fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .background(color = if (isDark) BoxBackground else TabBarTextColor, shape = RoundedCornerShape(15.dp))
                .height(50.dp),
                contentAlignment = Alignment.Center,
                ){

                val capitalizedStr = playlistName.replaceFirstChar { it.uppercaseChar() }

                Text(text = "${capitalizedStr} Playlist Song's", fontSize = 18.sp, fontFamily = jost,fontWeight = FontWeight.W600,)
            }

            Box(modifier = Modifier.fillMaxSize()) {

                if (playlistSongsList == null) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(16.dp),
                        color = if (isDark) AppThemeColor else Color.Red,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                } else if (playlistSongsList.isEmpty()) {
                    NotifyBanner("No Songs in this playlist. Search for a song and add it to this playlist.", isDark)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(playlistSongsList!!.size) {
                            PlaylistSongsItem(isDark,
                                playlistSongs = playlistSongsList!![it],
                                playerViewModel,
                                playlistViewModel,
                                onClick = {

                                    forSetSongsAsPlayer.clear()

                                    couroutineScope.launch {
                                        val forSetSongsAsPlayer = mutableListOf<SongData>()
                                        for (item in playlistSongsList!!) {
                                            forSetSongsAsPlayer.add(
                                                SongData(
                                                    item.songId,
                                                    item.title,
                                                    item.uri,
                                                    item.artist,
                                                    item.duration
                                                )
                                            )
                                        }
                                        playerViewModel.setPlayList(forSetSongsAsPlayer)
                                        playerViewModel.playAtIndex(it)
                                    }
                                })
                        }
                    }
                }


            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)){
            BottomMiniPlayer(isDark = isDark,onChangeTheme, playerViewModel, songViewModel,navController)
        }
    }
}


@Composable
fun PlaylistSongsItem(isDark: Boolean,playlistSongs: PlaylistSongs,playerViewModel: PlayerViewModel,playlistViewModel: PlaylistViewModel,onClick: ()-> Unit){

    var expanded by remember { mutableStateOf(false) }

    val menuItems = listOf("Remove Song","SongInfo")

    var selectedItem by remember { mutableStateOf(menuItems[0]) }


    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        }
        .height(height = 76.dp)
        .padding(vertical = 7.dp)){

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,

            ) {
            Box {


                DropdownMenu(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { androidx.compose.material.Text(item) },
                            onClick = {
                                selectedItem = item
                                when(selectedItem){
                                    "Remove Song" ->{
                                        coroutineScope.launch {
                                            playlistViewModel.removePlaylistSong(playlistSongs.playlistId,playlistSongs.songId)
                                        }
                                    }
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Row{
            Box(modifier = Modifier
                .width(70.dp)
                .padding(horizontal = 8.dp, vertical = 3.dp)
                .height(70.dp)){

                Box(modifier = Modifier
                    .width(60.dp)
                    .align(Alignment.Center)
                    .background(
                        color = if(isDark) BoxBackground else TabBarTextColor,
                        shape = RoundedCornerShape(14.dp),

                        )
                    .height(60.dp)){

                    Image(painter = painterResource(id = R.drawable.ic_favorite), contentDescription = "ic_favorite",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .width(40.dp)
                            .height(40.dp))

                }
            }

            Box(modifier = Modifier
                .fillMaxSize()){

                Row (modifier = Modifier.fillMaxSize(),horizontalArrangement = Arrangement.SpaceBetween,){
                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){
                        Text(modifier = Modifier.fillMaxWidth(0.5f), text = "${playlistSongs.title}",maxLines = 1,  fontSize = 14.sp, fontFamily = jost, fontWeight = FontWeight.W400)
                        Text(text = "MP3 ${playerViewModel.formatDuration(playlistSongs.duration)}", maxLines = 1, fontSize = 8.sp, fontWeight = FontWeight.W400, )
                    }


                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){

                        Row (horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 7.dp)){


                            }

                            IconButton(onClick = {

                                expanded = true

                            }) {
                                Icon(modifier = Modifier.size(15.dp), painter = painterResource(id = R.drawable.ic_menu), contentDescription = "Menu",)
                            }
                        }
                        }

                    }

                }

            }

        }

    }





@Preview(showBackground = true)
@Composable
fun PlaylistSongsScreenPreview(){

    val navController = rememberNavController()
    val playerViewModel : PlayerViewModel = viewModel()
    val songViewModel : SongViewModel = viewModel()
    val isDark : Boolean = false
    PlaylistSongsScreen(isDark, onChangeTheme = {}, playlistId = 0, playlistName = "", playerViewModel = playerViewModel, songViewModel = songViewModel, navController = navController)
}