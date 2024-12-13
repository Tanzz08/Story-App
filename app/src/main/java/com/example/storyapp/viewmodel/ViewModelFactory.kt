package com.example.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.UserPreference
import com.example.storyapp.data.di.Injection
import com.example.storyapp.data.repository.StoryRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val mStoryRepository: StoryRepository,
    private val mUserPreference: UserPreference
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(mStoryRepository, mUserPreference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    Injection.authRepository(context),
                    Injection.userPreference(context)
                )
            }.also { instance = it }
    }
}