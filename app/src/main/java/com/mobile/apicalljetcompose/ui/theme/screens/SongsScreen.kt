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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.BoxBackground
import com.mobile.apicalljetcompose.ui.theme.TabBarTextColor
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.Playlist
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.viewmodel.PlaylistViewModel
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(isDark : Boolean,onChangeTheme: () -> Unit, songViewModel: SongViewModel, playerViewModel: PlayerViewModel,playlistViewModel: PlaylistViewModel = viewModel(),navController: NavController) {



    val songList by songViewModel.songList.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val forSelectPlaylist by playlistViewModel.playlistList.observeAsState(emptyList())

    val forSelectionPlaylist = remember { mutableStateListOf<Long>() }

    val coroutineScope = rememberCoroutineScope()

    val songData = remember { mutableStateOf(SongData("0","","","",0)) }

    val forSetSongsAsPlayer = remember { mutableStateListOf<SongData>() }

    val context = LocalContext.current

    val isFav by songViewModel.currentSongItemfav.collectAsState()


    Box(modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier.fillMaxSize()){



            if (showBottomSheet){
                ModalBottomSheet(
                    modifier = Modifier.fillMaxSize(),
                    sheetState = sheetState,
                    onDismissRequest = {
                        forSelectionPlaylist.clear()
                        showBottomSheet = false
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize()){



                        Column (modifier = Modifier.fillMaxSize()){



                            Box(modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .height(50.dp)){
                                Row (modifier = Modifier.fillMaxSize()
                                    .align(Alignment.Center),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Row {
                                        Text("Selected Playlist - ${forSelectionPlaylist.size}", fontFamily = jost)
                                    }
                                    IconButton(onClick = {

                                        coroutineScope.launch {
                                            songViewModel.addTheSongToPlaylists(forSelectionPlaylist,songData)
                                            Toast.makeText(context,"Song Added to Playlists",Toast.LENGTH_SHORT).show()
                                            showBottomSheet = !showBottomSheet
                                            forSelectionPlaylist.clear()
                                        }

                                    }, colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color.Gray.copy(alpha = 0.2f),
                                        disabledContainerColor = Color.Gray.copy(alpha = 0.2f)
                                    )) {
                                        Icon(painter = painterResource(R.drawable.baseline_check_24), contentDescription = null)
                                    }
                                }
                            }

                            Box(modifier = Modifier.padding(5.dp)){
                                Divider(
                                    color = if (isDark) Color.White else Color.Black
                                )
                            }



                            Box(modifier = Modifier.fillMaxSize()){

                                if (forSelectPlaylist == null){
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(70.dp)
                                            .padding(16.dp),
                                        color = if (isDark) AppThemeColor else Color.Red,
                                        strokeWidth = 5.dp,
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                                if (forSelectPlaylist.isEmpty()){
                                    NotifyBanner("Please Navigate to playlist page to create the playlist and Add the songs",isDark)
                                }
                                forSelectPlaylist?.let {
                                    LazyColumn (modifier = Modifier.fillMaxSize()){
                                        items(forSelectPlaylist!!){
                                            ForSongPlaylistSelectionItem(isDark,it, modifier = Modifier,
                                                selectPlaylist = {
                                                forSelectionPlaylist.add(it)
                                            }, onRemovePlaylist = {
                                                forSelectionPlaylist.remove(it)
                                            },forSelectionPlaylist)
                                        }
                                    }
                                }
                            }
                        }






                    }
                }
            }



            LazyColumn (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){



                item{
                    if(songList.isEmpty()){
                        NotifyBanner("No Songs in your device storage you can intsall audio and play",isDark)
                    }
                    if (songList == null){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(70.dp)
                                .padding(16.dp),
                            color = if (isDark) AppThemeColor else Color.Red,
                            strokeWidth = 5.dp,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }

                items(songList.size) {
                    ListSongItem(isDark,songData = songList.get(it), onClick = {
                        CoroutineScope(Dispatchers.Main).launch{
                            forSetSongsAsPlayer.clear()
                            for (item in songList){
                                forSetSongsAsPlayer.add(SongData(item.id,item.name,item.song,item.artist,item.duration))
                            }
                            playerViewModel.setPlayList(forSetSongsAsPlayer)
                            playerViewModel.playAtIndex(it)
                        }
                    },modifier = Modifier,songViewModel, playerViewModel = playerViewModel,enbleBottomSheet = {showBottomSheet = !showBottomSheet}, setRef = {
                        songData.value = it
                    }, isFavOrNot = {},isFav)
                }
            }
        }
    }

}


@Composable
fun ListSongItem(isDark: Boolean,songData: SongData,onClick: () ->Unit, modifier: Modifier,songViewModel: SongViewModel,playerViewModel: PlayerViewModel,enbleBottomSheet : () -> Unit,setRef: (SongData) -> Unit,isFavOrNot: (String)-> Unit,isFavUi: Boolean){

    var expanded by remember { mutableStateOf(false) }

    var menuItems by remember { mutableStateOf(mutableListOf("Add To Playlist", "SongInfo")) }

    var selectedItem by remember { mutableStateOf(menuItems[0]) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current


    Log.d("Screen","sonis Fav ${songData.name} and ${isFavUi}")


    Box(modifier = modifier
        .fillMaxWidth()
        .height(height = 76.dp)
        .padding(vertical = 7.dp)
        .clickable {
            onClick()
            Log.d("Screen", "Song Clicked")
        }
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
            ) {
            Box {


                DropdownMenu(
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { androidx.compose.material.Text(item) },
                            onClick = {
                                selectedItem = item
                                when(selectedItem){
                                   "Make As Favorite" ->{
                                       coroutineScope.launch {
                                           songViewModel.addToFavorite(Favorite(0,songData.id,songData.name,songData.song,songData.artist,songData.duration),songData.id)
                                           Toast.makeText(context,"Songs Added to Favorite",Toast.LENGTH_SHORT).show()
                                       }
                                   }
                                    "Add To Playlist" ->{
                                        coroutineScope.launch {
                                            enbleBottomSheet()
                                            Toast.makeText(context,"Songs Added to Playlist",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    "Remove in Favorite" ->{
                                        coroutineScope.launch {
                                            songViewModel.removeSongFromFav(songData.id)
                                            Toast.makeText(context,"Songs Removed in Favorite",Toast.LENGTH_SHORT).show()
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

                    Image(painter = painterResource(id = R.drawable.ic_music), contentDescription = "ic_music",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .width(70.dp)
                            .height(70.dp))

                }
            }

            Box(modifier = Modifier
                .fillMaxSize()){

                Row (modifier = Modifier.fillMaxSize(),horizontalArrangement = Arrangement.SpaceBetween,){
                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){
                        Text(modifier = Modifier.fillMaxWidth(0.5f), text = "${songData.name}",maxLines = 1,  fontSize = 14.sp, fontFamily = jost, fontWeight = FontWeight.W400)
                        Text(text = "MP3 ${playerViewModel.formatDuration(songData.duration)}", maxLines = 1, fontSize = 8.sp, fontWeight = FontWeight.W400,)
                    }


                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){

                        Row (horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 7.dp)){


                            }
                            IconButton(onClick = {
                                setRef(songData)
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




@Composable
fun ForSongPlaylistSelectionItem(isDark: Boolean,playlist: Playlist, modifier: Modifier,selectPlaylist: (Long)-> Unit,onRemovePlaylist: (Long)->Unit,list: List<Long>){
    Box(modifier = modifier
        .fillMaxWidth()
        .height(height = 76.dp)
        .padding(vertical = 7.dp)){

        Row{
            Box(modifier = Modifier
                .width(70.dp)
                .padding(horizontal = 8.dp, vertical = 3.dp)
                .height(70.dp)){

                Box(modifier = Modifier
                    .width(60.dp)
                    .align(Alignment.Center)
                    .background(
                        color = if (isDark) BoxBackground else TabBarTextColor,
                        shape = RoundedCornerShape(14.dp),

                        )
                    .height(60.dp)){

                    Image(painter = painterResource(id = R.drawable.ic_playlist), contentDescription = "ic_playlist",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .width(30.dp)
                            .height(30.dp))

                }
            }

            Box(modifier = Modifier
                .fillMaxSize()){

                Row (modifier = Modifier.fillMaxSize(),horizontalArrangement = Arrangement.SpaceBetween,){
                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){
                        Text(modifier = Modifier.fillMaxWidth(0.5f), text = "${playlist.name}",maxLines = 1,  fontSize = 14.sp, fontFamily = jost, fontWeight = FontWeight.W400)
                        Text(text = "No of Songs : ${playlist.songsCount}", fontSize = 12.sp, fontFamily = jost,fontWeight = FontWeight.W400)
                    }


                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){

                        Row (horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 7.dp)){

                            }

                            if (list.contains(playlist.id.toLong())){
                                IconButton(onClick = {
                                    onRemovePlaylist(playlist.id.toLong())
                                }) {
                                    Icon(modifier = Modifier.size(25.dp), painter = painterResource(id = R.drawable.baseline_check_circle_24), contentDescription = "unselected",)
                                }
                            }else{
                                IconButton(onClick = {
                                    selectPlaylist(playlist.id.toLong())
                                }) {
                                    Icon(modifier = Modifier.size(25.dp), painter = painterResource(id = R.drawable.baseline_check_circle_outline_24), tint = Color.Gray.copy(alpha = 0.3f), contentDescription = "selected",)
                                }
                            }





                        }

                    }

                }

            }

        }

    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SongScreenPreview(){
    val navController = rememberNavController()
    val playerViewModel : PlayerViewModel = viewModel()
    val songViewModel : SongViewModel = viewModel()
    val isDark : Boolean = false
    SongsScreen(isDark, onChangeTheme = {},playerViewModel = playerViewModel, songViewModel = songViewModel,navController = navController)
}



