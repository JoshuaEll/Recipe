package com.example.recipe.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.recipe.presentation.BaseApplication
import com.example.recipe.presentation.components.CircularIndeterminateProgressBar
import com.example.recipe.presentation.components.LoadingRecipeShimmer
import com.example.recipe.presentation.components.RecipeView
import com.example.recipe.presentation.theme.AppTheme
import com.example.recipe.presentation.ui.recipe.RecipeEvent.*
import com.example.recipe.presentation.ui.recipe_list.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
const val IMAGE_HEIGHT = 260
@AndroidEntryPoint
class RecipeFragment : Fragment() {
    @Inject
    lateinit var application: BaseApplication

    private val viewModel: RecipeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeId")?.let { recipeId ->
            viewModel.onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        return ComposeView(requireContext()).apply { 
            setContent {
                val loading = viewModel.loading.value
                val recipe = viewModel.recipe.value

                val scaffoldState = rememberScaffoldState()
                AppTheme(darkTheme = application.isDark.value) {
                    Scaffold(
                        scaffoldState = scaffoldState
                    ) {
                        Box(modifier = Modifier.fillMaxSize()){
                            if(loading && recipe == null){
                                LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
                            }
                            else{
                                recipe?.let {
                                    RecipeView(recipe = it)
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