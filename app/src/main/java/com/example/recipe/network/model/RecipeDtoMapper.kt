package com.example.recipe.network.model

import com.example.recipe.domain.model.Recipe
import com.example.recipe.domain.util.DomainMapper

class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe>{
    override fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk,
            title = model.title,
            publisher = model.publisher,
            featuredImage = model.image,
            rating = model.rating,
            sourceUrl = model.sourceUrl,
            description = model.description,
            cookingInstructions = model.instructions,
            ingredients = model.ingredients?: listOf(),
            dateAdded = model.dateAdded,
            dateUpdated = model.dateUpdated
        )
    }

    // useless for this project since we are only getting not posting
    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            pk = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            image = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            description = domainModel.description,
            instructions = domainModel.cookingInstructions,
            ingredients = domainModel.ingredients,
            dateAdded = domainModel.dateAdded,
            dateUpdated = domainModel.dateUpdated
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe>{
        return initial.map { mapToDomainModel(it) }
    }
    fun fromDomainList(initial: List<Recipe>): List<RecipeDto>{
        return initial.map { mapFromDomainModel(it) }
    }

}