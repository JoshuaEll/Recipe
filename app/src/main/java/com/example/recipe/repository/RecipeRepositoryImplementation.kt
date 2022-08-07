package com.example.recipe.repository

import com.example.recipe.domain.model.Recipe
import com.example.recipe.network.RecipeService
import com.example.recipe.network.model.RecipeDtoMapper

class RecipeRepositoryImplementation
    (
    private val recipeService: RecipeService,
    private val mapper: RecipeDtoMapper
) : RecipeRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        // val result = recipeService.search(token, page, query).recipes
        // return mapper.toDomainList(result)
        return mapper.toDomainList(recipeService.search(token, page, query).recipes)
    }

    override suspend fun get(token: String, id: Int): Recipe {
        //val getter = recipeService.get(token, id)
        //return mapper.mapToDomainModel(getter)

        return mapper.mapToDomainModel(recipeService.get(token, id))
    }
}