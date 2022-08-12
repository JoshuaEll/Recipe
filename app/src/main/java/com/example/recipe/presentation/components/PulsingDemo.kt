package com.example.recipe.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp


@Composable
fun PulsingDemo(){
    val color = MaterialTheme.colors.primary
    val infinitTransition = rememberInfiniteTransition()
    val trans by infinitTransition.animateFloat(
        initialValue = PulseAnimationDefinition.INITIAL,
        targetValue = PulseAnimationDefinition.FINALE,
        animationSpec = infiniteRepeatable(
                        animation = tween(500, easing = FastOutLinearInEasing),
                        repeatMode = RepeatMode.Restart
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth()
                           .height(55.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterVertically)
                               .height(trans.dp)
                               .width(trans.dp),
            imageVector = Icons.Default.Favorite,
            contentDescription = "",
        )
    }


    Canvas(
        modifier = Modifier.fillMaxWidth()
            .height(55.dp),
    ){
        drawCircle(
            radius = trans,
            brush = SolidColor(color)
        )
    }
}
object PulseAnimationDefinition{
    val INITIAL = 40f
    val FINALE = 50f
}