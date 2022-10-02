package com.example.recipe.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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


@Composable
fun RecipeView(
    recipe: Recipe,
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())){
        recipe.featuredImage?.let { url ->
           AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(url).build(),
                placeholder = painterResource(R.drawable.empty_plate),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            recipe.title?.let { title ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ){
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h3
                    )
                    val rank = recipe.rating.toString()
                    Text(
                        text = rank,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h5)
                }
            }
        }
        recipe.publisher?.let { publisher ->
            val updated = recipe.dateUpdated
            Text(
                text = if(updated != null){
                    "Updated ${updated} by ${publisher}"
                }
                else{
                    "By ${publisher}"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.caption
            )
        }
        recipe.description?.let { description ->
            if(description != "N/A") {
                Text(
                    text = description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
        for (ingredient in recipe.ingredients){
            Text(
                text = ingredient,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                style = MaterialTheme.typography.body1
            )
        }
        recipe.cookingInstructions?.let { instructions ->
            Text(
                text = instructions,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                style = MaterialTheme.typography.body1
            )
        }
    }
}