package com.example.recipe.presentation.ui.recipe

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe.domain.model.Recipe
import com.example.recipe.presentation.ui.recipe.RecipeEvent.*
import com.example.recipe.repository.RecipeRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_RECIPE = "state.key.recipeId"
@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val recipeRepository: RecipeRepository,
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,

): ViewModel(){
    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val loading = mutableStateOf(false)
    init {
        state.get<Int>(STATE_KEY_RECIPE)?.let { recipe ->
            onTriggerEvent(GetRecipeEvent(recipe))
        }
    }

    fun onTriggerEvent(event: RecipeEvent){
        viewModelScope.launch {
            try{
                when(event){
                    is GetRecipeEvent -> {
                        if(recipe.value == null){
                            getRecipe(event.id)
                        }
                    }
                }
            }
            catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception ${e}, ${e.cause}")
            }
        }
    }

    private suspend fun getRecipe(id: Int){
        loading.value = true
        // simulate a delay to show loading
        delay(1000)

        val recipe = recipeRepository.get(token = token, id = id)
        this. recipe.value = recipe

        state.set(STATE_KEY_RECIPE, recipe.id)
        loading.value = false
    }
}