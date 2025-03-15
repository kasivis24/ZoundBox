plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.mobile.apicalljetcompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mobile.apicalljetcompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)





    // Navigation for Jetpack Compose
    implementation(libs.androidx.navigation.compose)

    // Lifecycle & ViewModel (MVVM)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)

    // Accompanist for Pager (HorizontalPager for TabLayout)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // Permissions (For reading audio files)
    implementation(libs.accompanist.permissions)

    // Coil (For Image Loading if needed)
    implementation(libs.coil.compose)

    // ExoPlayer core
    implementation(libs.androidx.media3.exoplayer)

    // UI components for player (Optional)
    implementation(libs.androidx.media3.ui)

    // Media session for handling playback controls (Optional)
    implementation(libs.androidx.media3.session)


    implementation(libs.coil.compose.v260)


    implementation(libs.androidx.room.runtime)

    kapt(libs.androidx.room.compiler)

    // Optional: Room with Kotlin Coroutines
    implementation(libs.androidx.room.ktx)


    // Retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)


    // StatusBar
    implementation(libs.accompanist.systemuicontroller)

    // RestartActivity
    implementation(libs.process.phoenix)

    // Like SharedPreferance
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}