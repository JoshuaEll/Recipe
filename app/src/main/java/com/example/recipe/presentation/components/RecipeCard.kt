package com.example.recipe.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipe.R
import com.example.recipe.domain.model.Recipe

// Composable View: This is the basic structure how every recipe shows up in the list of recipes

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
){
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(bottom = 6.dp, top = 6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp,
    ) {
        Column{
            // get the image check for null value
            recipe.featuredImage?.let { url ->
                // using Coils asyncimage function load the image belonging to the url
                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(url).build(),
                          placeholder = painterResource(R.drawable.empty_plate),
                          contentDescription = null,
                          modifier = Modifier.fillMaxWidth().height(225.dp),
                          contentScale = ContentScale.Crop

                          )

            }
            recipe.title?.let { title ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h3
                    )
                    Text(
                        text = recipe.rating.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h4
                        )
                }
            }
        }
    }
}