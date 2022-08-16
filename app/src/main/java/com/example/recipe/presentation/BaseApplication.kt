package com.example.recipe.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(){
    // should be stored in datastore or cache
    val isDark = mutableStateOf(false)

    //if called changes the boolean to the opposite
    fun toggleLightTheme(){
        isDark.value = !isDark.value
    }
}