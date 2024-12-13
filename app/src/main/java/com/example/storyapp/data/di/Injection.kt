package com.example.storyapp.data.di

import android.content.Context
import com.example.storyapp.data.ApiConfig
import com.example.storyapp.data.UserPreference
import com.example.storyapp.data.dataStore
import com.example.storyapp.data.database.StoryDatabase
import com.example.storyapp.data.repository.StoryRepository

object Injection {
    fun authRepository(context: Context): StoryRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, pref, database)
    }

    fun userPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

}