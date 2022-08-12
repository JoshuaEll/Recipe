package com.example.recipe.presentation.components

import android.graphics.Color
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

// THANKS TO JUAN GABRIEL IN THE YOUTUBE COMMENTS


@Composable
fun AnimatedHeartButton(
    modifier: Modifier,
    buttonState: MutableState<HeartAnimationDefinition.HeartButtonState>,
    onToggle: () -> Unit)
{

    val size by animateDpAsState(
        // Animation won't work if target value is always equal, this may be a jetpack compose bug?
        if(buttonState.value == HeartAnimationDefinition.HeartButtonState.ACTIVE) 50.1.dp else 50.dp,
        animationSpec = keyframes {
            durationMillis = 500
            HeartAnimationDefinition.expandedIconSize.at(100)
            HeartAnimationDefinition.idleIconSize.at(200)
        },
        finishedListener = {
            Log.d("DEBUG", "TEST ANIMATION")
        }
    )

    HeartButton(
        modifier = modifier,
        buttonState = buttonState,
        onToggle = onToggle,
        size = size
    )
}

@Composable private fun HeartButton(
    modifier: Modifier,
    buttonState: MutableState<HeartAnimationDefinition.HeartButtonState>,
    onToggle: () -> Unit,
    size: Dp
) {
    if(buttonState.value == HeartAnimationDefinition.HeartButtonState.ACTIVE) {
        Image(
            imageVector = Icons.Default.Favorite,
            modifier = modifier
                .size(size)
                .clickable(onClick = onToggle),
            contentDescription = "")
    }
    else {
        Image(
            imageVector = Icons.Default.FavoriteBorder,
            modifier = modifier
                .size(size)
                .clickable(onClick = onToggle),
            contentDescription = "")
    }
}

object HeartAnimationDefinition {

    enum class HeartButtonState { IDLE, ACTIVE }

    val idleIconSize = 50.dp
    val expandedIconSize = 80.dp
}
//enum class HeartDefinition{
//    IDLE, ACTIVE
//}
//@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
//@Composable
//fun HeartAnimation(){
//    var expanded by remember{ mutableStateOf(false)}
//
//
//    Surface(
//        onClick = { expanded to !expanded}
//    ) {
//        AnimatedContent(
//            targetState = expanded,
//            transitionSpec = {
//                fadeIn(animationSpec = tween(150, 150)) with
//                        fadeOut(animationSpec = tween(150)) using
//                        SizeTransform{ initialSize, targetSize ->
//                            if(targetState){
//                                keyframes {
//                                    IntSize(targetSize.width, targetSize.height) at 150
//                                    durationMillis = 300
//                                }
//                            }
//                            else{
//                                keyframes {
//                                    IntSize(initialSize.width, initialSize.height) at 150
//                                    durationMillis = 300
//                                }
//                            }
//                        }
//            }
//
//        ) { targetExpanded ->
//            if(targetExpanded){
//                Expanded()
//            }
//            else{
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(55.dp),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Image(
//                        modifier = Modifier
//                            .align(Alignment.CenterVertically)
//                            .height(80.dp)
//                            .width(80.dp),
//                        color = Color.RED,
//                        imageVector = Icons.Default.Favorite,
//                        contentDescription = )
//                }
//                ContentIcon()
//            }
//
//        }
//    }
//
//}
//
//fun Expanded(){
//
//}
//@Composable
//fun ContentIcon(){
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(55.dp),
//        horizontalArrangement = Arrangement.Center
//    ) {
//        Image(
//            modifier = Modifier.align(Alignment.CenterVertically)
//                .height(trans.dp)
//                .width(trans.dp),
//            imageVector = Icons.Default.Favorite,
//            contentDescription = "",
//        )
//    }
//}