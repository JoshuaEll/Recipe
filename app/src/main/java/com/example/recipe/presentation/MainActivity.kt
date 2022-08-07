package com.example.recipe.presentation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    //        val mapper = RecipeNetworkMapper()
//        val recipe = Recipe()
//        val networkEntity: RecipeNetworkEntity = mapper.mapToEntity(recipe)
    }
}