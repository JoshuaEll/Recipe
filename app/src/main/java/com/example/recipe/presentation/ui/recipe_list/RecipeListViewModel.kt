package com.example.recipe.presentation.ui.recipe_list

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipe.domain.model.Recipe
import com.example.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30


// Create an observable model for the Recipe list
@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    private @Named("auth_token") val token: String,
): ViewModel(){

    val recipes: MutableState<List<Recipe>> = mutableStateOf(ArrayList())
    val page = mutableStateOf(1)
    private var recipeListScrollPostion = 0
    //mutable state for the user query to stop losing the input of the user
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    var categoryScrollPosition: Int = 0
    val loading = mutableStateOf(false)

    init {
        newSearch()
    }

    fun newSearch(){
        viewModelScope.launch {
            loading.value = true
            resetSearchState()

            val result = repository.search(
                token = token,
                page = 1,
                query = query.value,
            )
            recipes.value = result
            loading.value = false
        }
    }

    fun nextPage(){
        viewModelScope.launch {
            // prevent duplicate events due to recompose happening too quickly
            if((recipeListScrollPostion + 1) >= (page.value * PAGE_SIZE)
            ){
                loading.value = true
                incrementPage()
                Log.d(TAG, "nextPage: triggered: ${page.value}")
                //just to show pagination
                //delay(1000)
                if(page.value > 1){
                    val result = repository.search(
                        token = token,
                        page = page.value,
                        query = query.value
                    )
                    appendRecipes(result)
                }
                loading.value = false
            }
        }
    }

    /*
     * Append new recipes to the current list of recipes
     */

    private fun appendRecipes(recipes: List<Recipe>){
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage(){
        page.value = page.value + 1
    }

    fun onChangeRecipeScrollPostion(position: Int){
        recipeListScrollPostion = position
    }

    // deselects the selected category
    private fun clearSelectedCategory(){
        selectedCategory.value = null
    }

    // clears the list and if the search bar value does not equal the selected chip clear the chip
    // this function also makes it so, that if a new category was chosen the user does not have to scroll all the way back up
    private fun resetSearchState(){
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPostion(0)
        // checks if the selected category does not matche the query value
        if(selectedCategory.value?.value != query.value && query.value == null) {
            clearSelectedCategory()
        }
        else if(selectedCategory.value?.value != query.value && query.value != null){
            clearSelectedCategory()
            val newCategory = getFoodCategory(query.value.replaceFirstChar { it.uppercase() })
            selectedCategory.value = newCategory
        }
    }


    // changes the default value displayed to the one the user typed
    // prevents loss of user input after screen rotation
    fun onQueryChanged(query: String){
        this.query.value =  query
    }

    // changes the query to whatever chip was clicked
    fun onSelectedCategoryChanged(category: String){
        val newCategory = getFoodCategory(category.replaceFirstChar { it.uppercase() })
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int){
        categoryScrollPosition = position
    }
}