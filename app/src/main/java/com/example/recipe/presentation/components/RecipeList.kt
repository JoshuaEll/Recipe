package com.example.recipe.presentation.components

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import com.example.recipe.R
import com.example.recipe.domain.model.Recipe
import com.example.recipe.presentation.ui.recipe_list.PAGE_SIZE
import com.example.recipe.presentation.ui.recipe_list.RecipeListEvent
import kotlin.coroutines.coroutineContext

@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerEvent: (RecipeListEvent) -> Unit,
    navController: NavController,
){
    // box holding shimmer loading, lazy column and circular loading bar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
    ){
        if(loading && recipes.isEmpty()){
            loadingRecipeListShimmer(imageHeight = 250.dp)
        }
        else
        {
            LazyColumn{
                itemsIndexed(
                    items = recipes
                ){ index, recipe ->
                    onChangeRecipeScrollPosition(index)
                    if((index + 1 ) >= (page * PAGE_SIZE) && !loading){
                        onTriggerEvent(RecipeListEvent.NextPageEvent)
                    }
                    RecipeCard(recipe = recipe, onClick = {
                        if(recipe.id != null) {
                            val bundle = Bundle()
                            bundle.putInt("recipeId", recipe.id)
                            navController.navigate(R.id.viewRecipe, bundle) //navigate to recipe
                        }
                    })
                }
            }
        }

        CircularIndeterminateProgressBar(isDisplayed = loading)
    }
}
