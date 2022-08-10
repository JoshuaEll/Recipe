package com.example.recipe.presentation.ui.recipe_list


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipe.presentation.components.FoodCategoryChip
import com.example.recipe.presentation.components.RecipeCard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select


// displays the list gotten from the viewModel using LazyColumn
@AndroidEntryPoint
class RecipeListFragment: Fragment() {
    val viewModel: RecipeListViewModel by viewModels()

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val recipes = viewModel.recipes.value
                val query = viewModel.query.value
                val keyboardController = LocalSoftwareKeyboardController.current
                val selectedCategory = viewModel.selectedCategory.value
                // since ScrollableRow does not work I have to work around it
                val scrollStatus = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()
                Column{
                    //using surface composable to give the search bar an elevation
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        elevation = 8.dp,
                    ){
                        Column {
                            Row{
                                //text field to let user enter stuff
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(8.dp),
                                    value = query,
                                    onValueChange = {
                                        viewModel.onQueryChanged(it)
                                    },
                                    label = {
                                        Text(text = "Search")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Search
                                    ),
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                                    },
                                    keyboardActions = KeyboardActions(onSearch = {
                                        viewModel.newSearch()
                                        //IMEActions didn't work
                                        keyboardController?.hide()
                                    }),
                                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = MaterialTheme.colors.surface
                                    )
                                )
                            }

                            // ScrollableRow does not exist anymore, so I just use a regular row with scrolling turned on
                            Row(modifier = Modifier
                                .horizontalScroll(scrollStatus)
                                .fillMaxWidth()
                                .padding(start = 8.dp, bottom = 8.dp)){
                                coroutineScope.launch {
                                    scrollStatus.scrollTo(viewModel.categoryScrollPosition)
                                }
                                for(category in getAllFoodCategories()){
                                    // implement the chip
                                    FoodCategoryChip(
                                        category = category.value,
                                        isSelected =  selectedCategory == category,
                                        onSelectedCategoryChanged = {
                                          viewModel.onSelectedCategoryChanged(it)
                                            viewModel.onChangeCategoryScrollPosition(scrollStatus.value)
                                        },
                                        onExecuteSearch = viewModel::newSearch
                                    )
                                }
                            }
                        }
                    }

                    //LazyColumn outside of surface, so that it does not get the background color
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
}