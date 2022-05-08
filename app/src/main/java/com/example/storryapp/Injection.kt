package com.example.storryapp

import android.content.Context
import com.example.storryapp.data.StoryRepository
import com.example.storryapp.data.network.retrofit.ApiConfig
import com.example.storryapp.view.database.StoryDatabase

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getInstance(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}