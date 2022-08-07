package com.example.recipe.di


import com.example.recipe.network.RecipeService
import com.example.recipe.network.model.RecipeDtoMapper
import com.example.recipe.repository.RecipeRepository
import com.example.recipe.repository.RecipeRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
// Repository dependencies for injection
// This could be done in the RecipeRepositoryImplementation file, but doing it this  way makes it easier for testing and understandability
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
{
    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository{
        return RecipeRepositoryImplementation(recipeService, recipeDtoMapper)
    }
}