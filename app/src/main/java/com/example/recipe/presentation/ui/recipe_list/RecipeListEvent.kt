package com.example.recipe.presentation.ui.recipe_list

sealed class RecipeListEvent{
    object NewSearchEvent: RecipeListEvent()
    object NextPageEvent: RecipeListEvent()
    // restores after process death
    object  RestoreStateEvent: RecipeListEvent()
}