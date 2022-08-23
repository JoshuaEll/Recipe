package com.example.recipe.presentation.ui.recipe_list


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.example.recipe.presentation.BaseApplication
import com.example.recipe.presentation.components.*
import com.example.recipe.presentation.components.HeartAnimationDefinition.HeartButtonState.*
import com.example.recipe.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import javax.inject.Inject


// displays the list gotten from the viewModel using LazyColumn
@AndroidEntryPoint
class RecipeListFragment: Fragment() {
    @Inject
    lateinit var application: BaseApplication

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
                AppTheme(darkTheme = application.isDark.value
                ) {
                    val recipes = viewModel.recipes.value
                    val query = viewModel.query.value

                    val selectedCategory = viewModel.selectedCategory.value
                    val loading = viewModel.loading.value

                    val page = viewModel.page.value
                    Scaffold(
                        topBar = {
                            //using surface composable to give the search bar an elevation
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = viewModel::newSearch,
                                scrollPosition = viewModel.categoryScrollPosition,
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = {
                                    application.toggleLightTheme()
                                }
                            )
                        },

                    ) {
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
                                        viewModel.onChangeRecipeScrollPostion(index)
                                        if((index + 1 ) >= (page * PAGE_SIZE) && !loading){
                                            viewModel.nextPage()
                                        }
                                        RecipeCard(recipe = recipe, onClick = {})
                                    }
                                }
                            }

                            CircularIndeterminateProgressBar(isDisplayed = loading)
                        }
                    }

                }



            }
        }
    }
}
//Example
@Composable
fun MyBottomBar(

){
    BottomNavigation(
        elevation = 12.dp
    ) {
        BottomNavigationItem(icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = ""
            )
        },
            selected = false,
            onClick = {

            }
        )
        BottomNavigationItem(icon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = ""
            )
        },
            selected = true,
            onClick = {

            }
        )
        BottomNavigationItem(icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = ""
            )
        },
            selected = false,
            onClick = {

            }
        )
    }

}
//Example
@Composable
fun MyDrawer(){
    Column(){
        Text("Item1")
        Text("Item2")
        Text("Item3")
        Text("Item4")
        Text("Item5")
    }
}