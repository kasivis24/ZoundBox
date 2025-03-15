package com.mobile.apicalljetcompose.ui.theme.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import com.mobile.apicalljetcompose.R
import com.mobile.apicalljetcompose.ui.theme.constants.AppConstants
import com.mobile.apicalljetcompose.ui.theme.jost

@Composable
fun SplashScreen(navController: NavController) {


    val systemUiController = rememberSystemUiController()

    // Set status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }

    // Using LaunchedEffect to navigate after delay
    LaunchedEffect(Unit) {
        delay(2000) // Delay for 2 seconds
        navController.navigate(AppConstants.TAB_SCREEN_ROUTE) {
            popUpTo(AppConstants.SPLASH_SCREEN_ROUTE) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo), // Replace with your logo
            contentDescription = "Splash Logo",
            modifier = Modifier.size(200.dp)
                .align(Alignment.Center)
        )

        Text(modifier = Modifier.align(Alignment.BottomCenter), text = "ZoundBox", fontSize = 20.sp, fontFamily = jost,fontWeight = FontWeight.W500, color = Color.Gray.copy(alpha = 0.3f))


    }
}


