package com.example.recipe.presentation.ui.recipe_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.domain.model.Recipe
import com.example.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

// Create an observable model for the Recipe list
@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    private @Named("auth_token") val token: String,
): ViewModel(){

    val recipes: MutableState<List<Recipe>> = mutableStateOf(ArrayList())

    //mutable state for the user query to stop losing the input of the user
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    var categoryScrollPosition: Int = 0

    init {
        newSearch()
    }

    //will be changed later to allow user searching
    fun newSearch(){
        viewModelScope.launch {
            val result = repository.search(
                token = token,
                page = 1,
                query = query.value,
            )
            recipes.value = result
        }
    }

    // changes the default value displayed to the one the user typed
    // prevents loss of user input after screen rotation
    fun onQueryChanged(query: String){
        this.query.value =  query
    }

    // changes the query to whatever chip was clicked
    fun onSelectedCategoryChanged(category: String){
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int){
        categoryScrollPosition = position
    }
}