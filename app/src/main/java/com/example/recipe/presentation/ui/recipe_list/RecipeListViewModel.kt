package com.example.recipe.presentation.ui.recipe_list

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.core.view.ViewCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipe.domain.model.Recipe
import com.example.recipe.repository.RecipeRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30
const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

// Create an observable model for the Recipe list
@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    private @Named("auth_token") val token: String,
    private val savedStateHandle: SavedStateHandle,
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
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { l ->
            setListScrollPostion(l)
        }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }
        if(recipeListScrollPostion != 0){
            onTriggerEvent(RecipeListEvent.RestoreStateEvent)
        }
        else{
            onTriggerEvent(RecipeListEvent.NewSearchEvent)
        }

    }

    // created for unidirectional flow
    //  useful when abstract use cases
    fun onTriggerEvent(event: RecipeListEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is RecipeListEvent.NewSearchEvent -> {
                        newSearch()
                    }
                    is RecipeListEvent.NextPageEvent -> {
                        nextPage()
                    }
                    is RecipeListEvent.RestoreStateEvent -> {
                        restoreState()
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    // restores state after termination
    private suspend fun restoreState() {
        loading.value = true
        val results: MutableList<Recipe> = mutableListOf()
        // in production we would not do this rather have a cache and query that instead
        for (p in 1..page.value){
            val result = repository.search(
                token = token,
                page = p,
                query = query.value
            )
            results.addAll(result)
            if(p == page.value){ //done
                recipes.value = results
                loading.value = false
            }
        }
    }

    private suspend fun newSearch(){
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

    private suspend fun nextPage(){

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

    /*
     * Append new recipes to the current list of recipes
     */

    private fun appendRecipes(recipes: List<Recipe>){
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage(){
        setPage(page.value + 1)
    }

    fun onChangeRecipeScrollPostion(position: Int){
        setListScrollPostion(position = position)
    }

    // deselects the selected category
    private fun clearSelectedCategory(){
        setSelectedCategory(null)
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
        setQuery(query)
    }

    // changes the query to whatever chip was clicked
    fun onSelectedCategoryChanged(category: String){
        val newCategory = getFoodCategory(category.replaceFirstChar { it.uppercase() })
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int){
        categoryScrollPosition = position
    }

    private fun setListScrollPostion(position: Int){
        recipeListScrollPostion = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int){
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?){
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String){
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}