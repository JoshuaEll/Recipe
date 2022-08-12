package com.example.recipe.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// a spinning progressbar while items are loading
@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean,
){
    if(isDisplayed){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }
}