package com.example.recipe.presentation.ui.recipe_list


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipe.presentation.components.RecipeCard
import dagger.hilt.android.AndroidEntryPoint

// displays the list gotten from the viewModel using LazyColumn
@AndroidEntryPoint
class RecipeListFragment: Fragment() {
    val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val recipes = viewModel.recipes.value
                LazyColumn{
                    itemsIndexed(
                        items = recipes
                    ){ index, recipe ->
                        RecipeCard(recipe = recipe, onClick = {})
                        }
                    }
                }
            }
        }
}