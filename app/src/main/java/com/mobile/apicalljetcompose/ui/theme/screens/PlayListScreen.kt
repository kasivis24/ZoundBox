package com.mobile.apicalljetcompose.ui.theme.screens
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.BoxBackground
import com.mobile.apicalljetcompose.ui.theme.TabBarTextColor
import com.mobile.apicalljetcompose.ui.theme.constants.AppConstants
import com.mobile.apicalljetcompose.ui.theme.data.Playlist
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.thememanager.ThemePreferenceManager
import com.mobile.apicalljetcompose.ui.theme.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(isDark : Boolean,onChangeTheme: () -> Unit,playlistViewModel: PlaylistViewModel = viewModel(),navController: NavController,themePreferenceManager: ThemePreferenceManager) {




    val openDialog = remember { mutableStateOf(false)}
    var createPlaylistText by remember { mutableStateOf(TextFieldValue("")) }
    val playlistData by playlistViewModel.playlistList.observeAsState()


    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false )

    var showBottomSheet by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current







    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.5f),
                sheetState = sheetState,
                onDismissRequest = {
                    showBottomSheet = !showBottomSheet
                }
            ){


                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Icon(
                            painter = if (isDark) painterResource(R.drawable.baseline_dark_mode_24) else painterResource(R.drawable.baseline_light_mode_24),
                            contentDescription = "Dark Mode Icon",
                            tint = if (isDark) AppThemeColor else Color.Red,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = if (isDark) "Dark Mode" else "Light Mode", fontSize = 20.sp, fontFamily = jost)
                        Spacer(modifier = Modifier.height(16.dp))
                        Switch(
                            checked = isDark,
                            onCheckedChange = { newValue ->
                                scope.launch {
                                    onChangeTheme()
                                    themePreferenceManager.saveTheme(newValue)
//                                    ProcessPhoenix.triggerRebirth(context)
                                }
                            }
                        )
                    }
                }


            }
        }




        if (openDialog.value) {
            AlertDialog(
                shape = RoundedCornerShape(10.dp),
                backgroundColor = if (isDark) BoxBackground else TabBarTextColor,
                onDismissRequest = { openDialog.value = false },
                title = {
                    Text(
                        text = "Create the playlist",
                        fontFamily = jost,
                        fontSize = 18.sp,
                        color = if (isDark) AppThemeColor else Color.Black,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center,
                    )

                },
                text = {
                    TextField(
                        value = createPlaylistText,
                        onValueChange = {
                            createPlaylistText = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = if (isDark) BoxBackground else TabBarTextColor,
                            focusedIndicatorColor = if (isDark) AppThemeColor else Color.Red,
                            unfocusedIndicatorColor = if (isDark) AppThemeColor else Color.Red,
                            textColor = if (isDark) TabBarTextColor else BoxBackground,
                        ),
                        placeholder = {

                            Text(
                                text = "Add New Playlists",
                                fontFamily = jost,
                                fontSize = 13.sp,
                                color = if (isDark) TabBarTextColor else Color.Black,
                                fontWeight = FontWeight.W400,
                                textAlign = TextAlign.Center,
                            )

                        },
                        modifier = Modifier.background(if (isDark)BoxBackground else TabBarTextColor)
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (!createPlaylistText.text.isEmpty()){
                            playlistViewModel.createPlayList(createPlaylistText.text)
                        }
                        openDialog.value = false
                    }) {
                        Text("OK",
                            fontFamily = jost,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = if(isDark) AppThemeColor else Color.Red,
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("Cancel",
                            fontFamily = jost,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = if(isDark) AppThemeColor else Color.Red,
                        )
                    }
                }
            )
        }


        playlistData?.let {
            LazyColumn (modifier = Modifier.fillMaxSize()){
                item {
                    Column (modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 5.dp)){


                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp, 5.dp, 15.dp, 5.dp)
                            .background(TabBarTextColor, shape = RoundedCornerShape(5.dp))
                            .clickable {
                                navController.navigate(AppConstants.SEARCH_SCREEN_ROUTE)
                            }
                            .height(40.dp),
                            contentAlignment = Alignment.Center
                            ){

                            Row (modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_search_24),
                                    contentDescription = "search",
                                    modifier = Modifier
                                        .clickable {

                                        }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                androidx.compose.material.Text(
                                    text = "Search the song here..",
                                    fontFamily = jost,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.W500,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }


                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openDialog.value = true
                            }
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                            .height(35.dp)){
                            Row (modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){


                               Row (verticalAlignment = Alignment.CenterVertically){
                                   Image(
                                       painter = painterResource(id = R.drawable.baseline_add_24,),
                                       contentDescription = "search",
                                       colorFilter = if(isDark) ColorFilter.tint(AppThemeColor) else ColorFilter.tint(Color.Red),
                                       modifier = Modifier
                                           .size(20.dp)
                                           .clickable {

                                           }
                                   )


                                   Spacer(modifier = Modifier.width(5.dp))

                                   Text(text = "Create Playlists",
                                       fontFamily = jost,
                                       fontSize = 15.sp,
                                       color = if(isDark) AppThemeColor else Color.Red,
                                       fontWeight = FontWeight.W500,
                                       textAlign = TextAlign.Center,
                                   )
                               }


                                IconButton(onClick = {
                                    showBottomSheet = !showBottomSheet
                                }) {
                                    Icon(painter = painterResource(R.drawable.baseline_color_lens_24), contentDescription = null, tint = if (isDark) AppThemeColor else Color.Red)
                                }

                            }
                        }

                    }
                }

                if (playlistData!!.isEmpty()){
                    item {
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            ){
                            NotifyBanner("You are not yet create the playlist you can create playlist and add the songs ",isDark)
                        }
                    }
                }

                items(playlistData!!){
                    ListPlayListItem(isDark,playlist = it,playlistViewModel, modifier = Modifier,navController, onClick = {
                        try {
                            Log.d("Screen","Naigate ${it.id}")
                            val playlistId = it.id
                            val playlistName = Uri.encode(it.name)
                            navController.navigate("${AppConstants.PLAYLIST_SCREEN_ROUTE}/$playlistId/$playlistName")
                        }catch (e:Exception){
                            Log.d("Screen","issue ${e}")
                        }
                    })
                }
            }
        } ?: CircularProgressIndicator(
        modifier = Modifier
            .size(70.dp)
            .padding(16.dp),
        color = if (isDark) AppThemeColor else Color.Red,
        strokeWidth = 5.dp,
        strokeCap = StrokeCap.Round
        )

    }
}


@Composable
fun ListPlayListItem(isDark: Boolean,playlist: Playlist,playlistViewModel: PlaylistViewModel,modifier: Modifier,navController: NavController,onClick: ()-> Unit){


    var expanded by remember { mutableStateOf(false) }

    val menuItems = listOf("Delete Playlist","Playlist Info")

    var selectedItem by remember { mutableStateOf(menuItems[0]) }


    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Box(modifier = modifier
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
                        .height(100.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { item ->
                        DropdownMenuItem(
                            text = { androidx.compose.material.Text(item) },
                            onClick = {
                                selectedItem = item

                                when(selectedItem){
                                    "Delete Playlist" ->{
                                        coroutineScope.launch {
                                            playlistViewModel.deletePlaylist(playlist.id)
                                            Toast.makeText(context,"Playlist Deleted",Toast.LENGTH_SHORT).show()
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
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(modifier = Modifier.size(15.dp), painter = painterResource(id = R.drawable.ic_menu), contentDescription = "Menu",)
                            }
                        }

                    }

                }

            }

        }

    }
}



