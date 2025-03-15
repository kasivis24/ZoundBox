import android.content.Context
import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.AppThemeColor
import com.mobile.apicalljetcompose.ui.theme.BoxBackground
import com.mobile.apicalljetcompose.ui.theme.TabBarTextColor
import com.mobile.apicalljetcompose.ui.theme.data.CloudSongs
import com.mobile.apicalljetcompose.ui.theme.data.Favorite
import com.mobile.apicalljetcompose.ui.theme.data.SongData
import com.mobile.apicalljetcompose.ui.theme.jost
import com.mobile.apicalljetcompose.ui.theme.screens.BottomMiniPlayer
import com.mobile.apicalljetcompose.ui.theme.screens.ForSongPlaylistSelectionItem
import com.mobile.apicalljetcompose.ui.theme.screens.NotifyBanner
import com.mobile.apicalljetcompose.ui.theme.utils.SpeechRecognizerHelper
import com.mobile.apicalljetcompose.ui.theme.viewmodel.PlaylistViewModel
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SearchViewModel
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun NoInternetDialog(isDark: Boolean,onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* Prevent dismissal */ },
        title = { Text(text = "No Internet Connection", color = if (isDark) AppThemeColor else Color.Red, fontFamily = jost) },
        text = { Text(text = "Please check your network settings and try again.",  color = if (isDark) AppThemeColor else Color.Red,fontFamily = jost) },
        confirmButton = {
            Text(modifier = Modifier.clickable {
                onRetry()
            }, text = "Retry",  color = if (isDark) AppThemeColor else Color.Red, fontFamily = jost)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(isDark : Boolean, onChange: () -> Unit, playerViewModel: PlayerViewModel, songViewModel: SongViewModel, playlistViewModel: PlaylistViewModel = viewModel(), navController: NavController, searchViewModel: SearchViewModel = viewModel()){



    Log.d("Screen","it is search")
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue("Search Songs"))
    }

    val forSelectPlaylist by playlistViewModel.playlistList.observeAsState(emptyList())

    val allCloudSongs by searchViewModel.filteredSongs.observeAsState()

    val searchQuery = searchViewModel.searchQuery

    val scope = rememberCoroutineScope()

    val forSetSongsAsPlayer = remember { mutableStateListOf<SongData>() }

    var showDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val sheetStatePlaylist = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    var showBottomSheet by remember { mutableStateOf(false) }


    val forSelectionPlaylist = remember { mutableStateListOf<Long>() }

    val coroutineScope = rememberCoroutineScope()

    val songData = remember { mutableStateOf(SongData("0","","","",0)) }

    val context = LocalContext.current
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }



    val systemUiController = rememberSystemUiController()

    // Set status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }




    LaunchedEffect(Unit) {
        isConnected.value = checkInternetConnection(context)
    }

    if (!isConnected.value) {
        NoInternetDialog(isDark) { isConnected.value = checkInternetConnection(context) }
    }


    val speechHelper = remember { mutableStateOf<SpeechRecognizerHelper?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                speechHelper.value?.destroy()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            speechHelper.value?.destroy()
        }
    }


    Box(modifier = Modifier.fillMaxSize()){


        if (showDialog) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = {
                    showDialog = !showDialog
                }
            ){

                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Image(
                        colorFilter = ColorFilter.tint(color = if (isDark) AppThemeColor else Color.Red),
                        alignment = Alignment.Center,
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.mic_gif) // Replace with your GIF file
                                .build()
                        ),
                        contentDescription = "GIF Image"
                    )

                    Text("Speak to search songs",
                        fontFamily = jost,
                        fontSize = 20.sp,
                        color = Color.Gray.copy(alpha = 0.3f),
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center,
                    )
                }

            }
        }



        if (showBottomSheet){
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetStatePlaylist,
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
                                    androidx.compose.material3.Text(
                                        "Selected Playlist - ${forSelectionPlaylist.size}",
                                        fontFamily = jost
                                    )
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
                                    androidx.compose.material3.Icon(
                                        painter = painterResource(R.drawable.baseline_check_24),
                                        contentDescription = null
                                    )
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
                                androidx.compose.material3.CircularProgressIndicator(
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

        Column (modifier = Modifier.fillMaxSize()){

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 15.dp)
                ){

                OutlinedTextField(
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = jost,
                    ),
                    placeholder = {
                        Text("Search Here",
                            fontFamily = jost,
                            fontSize = 16.sp,
                            color = if (isDark) Color.White else Color.Black,
                            fontWeight = FontWeight.W500,
                            textAlign = TextAlign.Center,
                            )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery.value,
                    trailingIcon = {
                        Box(modifier = Modifier){
                            IconButton(onClick = {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ActivityCompat.requestPermissions(
                                        (context as android.app.Activity),
                                        arrayOf(Manifest.permission.RECORD_AUDIO),
                                        1
                                    )
                                    showDialog = !showDialog
                                }
                                speechHelper.value = SpeechRecognizerHelper(context,
                                    onResult = { result ->
                                        searchViewModel.searchQuery.value = result
                                        searchViewModel.updateFilteredSongs()
                                        showDialog = !showDialog
                                    },
                                    onError = { error ->
                                        showDialog = !showDialog
                                        Toast.makeText(context, "Speak again", Toast.LENGTH_SHORT).show()
                                    })
                                speechHelper.value?.startListening()
                            }) {
                                Image(
                                    painter = painterResource(R.drawable.baseline_mic_24),
                                    colorFilter = if(isDark) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black),
                                    contentDescription = "ic_mic",
                                    modifier = Modifier
                                        .size(23.dp)
                                )
                            }
                        }
                    },
                    leadingIcon = {
                        Box(modifier = Modifier){
                            Image(
                                painter = painterResource(id = R.drawable.baseline_search_24,
                                ),
                                colorFilter = if(isDark) ColorFilter.tint(Color.White) else ColorFilter.tint(Color.Black),
                                contentDescription = "ic_search",
                                modifier = Modifier
                                    .size(23.dp)
                            )
                        }
                    },

                    onValueChange = {
                        Log.d("Screen","onvalue")
                        scope.launch {
                            searchQuery.value = it
                            searchViewModel.updateFilteredSongs()
                        }
                    },
//                    label = {
//                        Text("Search Here",
//                            fontFamily = jost,
//                            fontSize = 16.sp,
//                            color = Color.White,
//                            fontWeight = FontWeight.W500,
//                            textAlign = TextAlign.Center,
//                        )
//                    },
                    shape = RoundedCornerShape(13.dp),
                    singleLine = true,
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Gray.copy(alpha = 0.5f),
                        focusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                        disabledBorderColor = Color.Gray.copy(alpha = 0.5f),
                        cursorColor = if (isDark) AppThemeColor else Color.Red,
                    ),

                    )

            }




            Box(modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)){
                allCloudSongs?.let {
                        LazyColumn {
                        allCloudSongs?.let { it1 ->
                            items(it1.size){
                                RecentSearchItem(isDark,cloudSongs = allCloudSongs!!.get(it), onClick = {
                                    forSetSongsAsPlayer.clear()
                                    Log.d("Screen","fun  $it1");

                                    CoroutineScope(Dispatchers.Main).launch {
                                        for (item in it1){
                                            Log.d("Screen","${item.songTitle}");
                                            forSetSongsAsPlayer.add(SongData(item.songId,item.songTitle,item.songUri,item.artistName,0))
                                        }
                                        playerViewModel.setPlayList(forSetSongsAsPlayer)
                                        playerViewModel.playAtIndex(it)
                                    }
                                }, modifier = Modifier, playerViewModel = playerViewModel, enbleBottomSheet = {showBottomSheet = !showBottomSheet}, setRef = {
                                    songData.value = SongData(it.songId,it.songTitle,it.songUri,it.artistName,0)
                                })
                            }
                        }
                    }
                }?: Box(modifier = Modifier.fillMaxSize()){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = if (isDark) AppThemeColor else Color.Red,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                }

            }

        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)){
            BottomMiniPlayer(isDark = isDark,onChange, playerViewModel,songViewModel,navController)
        }
    }
}




@Composable
fun RecentSearchItem(isDark: Boolean,cloudSongs: CloudSongs, onClick: () ->Unit, modifier: Modifier,playerViewModel: PlayerViewModel, enbleBottomSheet : () -> Unit, setRef: (CloudSongs) -> Unit){

    var expanded by remember { mutableStateOf(false) }

    var menuItems = listOf("Add To Playlist","SongInfo")

    var selectedItem by remember { mutableStateOf(menuItems[0]) }

    val coroutineScope = rememberCoroutineScope()

    val isFav = playerViewModel.isFav.collectAsState()

    val context = LocalContext.current






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
                                    "Add To Playlist" ->{
                                        coroutineScope.launch {
                                            enbleBottomSheet()
                                            Toast.makeText(context,"Songs Added to Playlist", Toast.LENGTH_SHORT).show()
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
                        androidx.compose.material3.Text(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            text = "${cloudSongs.songTitle}",
                            maxLines = 1,
                            fontSize = 14.sp,
                            fontFamily = jost,
                            fontWeight = FontWeight.W400
                        )

                        androidx.compose.material3.Text(
                            text = "MP3 ${
                                playerViewModel.formatDuration(
                                    22323
                                )
                            }",
                            maxLines = 1,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.W400,
                        )
                    }


                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){

                        Row (horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 7.dp)){

                            }
                            IconButton(onClick = {
                                setRef(cloudSongs)
                                expanded = true
                            }) {
                                androidx.compose.material3.Icon(modifier = Modifier.size(15.dp), painter = painterResource(id = R.drawable.ic_menu), contentDescription = "Menu",)
                            }
                        }

                    }

                }

            }

        }

    }
}

fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen(){
    val navController = rememberNavController()
    val playerViewModel : PlayerViewModel = viewModel()
    val songViewModel : SongViewModel = viewModel()
    SearchScreen(isDark = true, onChange = {}, playerViewModel = playerViewModel,songViewModel,navController = navController)
}