package com.mobile.apicalljetcompose.ui.theme.screens

import PlayerViewModel
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.BoxBackground
import com.mobile.apicalljetcompose.ui.theme.TabBarTextColor
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(isDark : Boolean, onChange : () -> Unit, viewModel: FavoriteViewModel = viewModel(),playerViewModel: PlayerViewModel,navController: NavController) {


    val favoriteSongsList by viewModel.favoriteList.observeAsState(emptyList())

    val forSetSongsAsPlayer = remember { mutableStateListOf<SongData>() }

    val couroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {




        if (favoriteSongsList == null){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(70.dp)
                    .padding(16.dp),
                color = if (isDark) AppThemeColor else Color.Red,
                strokeWidth = 5.dp,
                strokeCap = StrokeCap.Round
            )
        }
        favoriteSongsList?.let {
            LazyColumn (modifier = Modifier.fillMaxSize()){
                items (favoriteSongsList!!.size){
                    ListFavoriteItem(isDark,favoriteSongsList!!.get(it),viewModel, playerViewModel = playerViewModel,onClick = {
                        couroutineScope.launch {
                            forSetSongsAsPlayer.clear()
                            for (item in favoriteSongsList!!){
                                Log.d("Screen",item.songId.toString())
                                forSetSongsAsPlayer.add(SongData(item.songId.toString(),item.title,item.uri,item.artist,item.duration))
                            }
                            playerViewModel.setPlayList(forSetSongsAsPlayer)
                            playerViewModel.playAtIndex(it)
                        }
                    })
                }
            }
        }

        if (favoriteSongsList.isEmpty()){
            NotifyBanner("Your'e not selected any song as favorite please make song as favorite and play favorite song",isDark)
        }

    }
}


@Composable
fun ListFavoriteItem(isDark: Boolean,favorite: Favorite,favoriteViewModel: FavoriteViewModel,playerViewModel: PlayerViewModel,onClick: ()-> Unit){



    var expanded by remember { mutableStateOf(false) }

    val menuItems = listOf("Remove In Favorite","SongInfo")

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
                                    "Remove In Favorite" ->{
                                        coroutineScope.launch {
                                            favoriteViewModel.removeSongFromFav(favorite.songId)
                                            Toast.makeText(context,"Song removed Succesfully",Toast.LENGTH_SHORT).show()
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
                        Text(modifier = Modifier.fillMaxWidth(0.5f), text = "${favorite.title}",maxLines = 1,  fontSize = 14.sp, fontFamily = jost, fontWeight = FontWeight.W400)
                        Text(text = "MP3 ${playerViewModel.formatDuration(favorite.duration)}", maxLines = 1, fontSize = 8.sp, fontWeight = FontWeight.W400, )
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


