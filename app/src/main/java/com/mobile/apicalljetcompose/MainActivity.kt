package com.mobile.apicalljetcompose

import PlayerViewModel
import SearchScreen
import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

import com.mobile.apicalljetcompose.ui.theme.ThemeAndColorTheme
import com.mobile.apicalljetcompose.ui.theme.route.AppNavigation

import com.mobile.apicalljetcompose.ui.theme.screens.TabScreen
import com.mobile.apicalljetcompose.ui.theme.thememanager.ThemePreferenceManager
import com.mobile.apicalljetcompose.ui.theme.utils.RequestPermission
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var themePreferenceManager: ThemePreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        themePreferenceManager = ThemePreferenceManager(this)
        setContent {
            var isDark by remember { mutableStateOf(false)}

            LaunchedEffect(Unit) {
                themePreferenceManager.themeFlow.collect { savedTheme ->
                    isDark = savedTheme
                }
            }
            ThemeAndColorTheme (darkTheme = isDark){
                Scaffold(
                    modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                        Box(modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()){



                            val navController = rememberNavController()
                            val playerViewModel : PlayerViewModel = viewModel()
                            val songViewModel : SongViewModel = viewModel()

                            val context = LocalContext.current

                            val scope = rememberCoroutineScope()

                            val songList by songViewModel.songList.collectAsState()
                            RequestPermission {
                                LaunchedEffect(Unit) {
                                    scope.launch {
                                        songViewModel.fetchAudio(context)
                                        Log.d("Screen","Fetch")
                                    }
                                }
                            }

                            LaunchedEffect(songList) {
                                if (!songList.isEmpty()) {
                                    playerViewModel.setPlayList(songList)
                                    Log.d("Screen","Set")
                                }
                            }



                            AppNavigation(isDark, onChange = {isDark = !isDark},playerViewModel,songViewModel,navController,themePreferenceManager)
                        }

                    }

                }
            }
        }
    }
}



