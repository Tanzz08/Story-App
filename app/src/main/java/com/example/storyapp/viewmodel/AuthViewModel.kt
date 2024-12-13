package com.example.storyapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.ResultState
import com.example.storyapp.data.UserPreference
import com.example.storyapp.data.database.StoryEntity
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.FileUploadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File

class AuthViewModel(private val repository: StoryRepository, val userPreference: UserPreference) :
    ViewModel() {


    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)

    fun login(email: String, password: String) = repository.login(email, password)

    fun logout(){
        viewModelScope.launch {
            userPreference.logout()
        }
    }

    fun getStories(): Flow<PagingData<StoryEntity>> {
        return repository.getStories().cachedIn(viewModelScope)
    }

    fun getStoriesWithLocation() = repository.getStoriesWithLocation()

    fun uploadImage(
        token: String,
        file: File,
        description: String,
        lat: Float,
        lon: Float
    ): Flow<ResultState<FileUploadResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = repository.uploadImage(token, file, description, lat, lon)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun getToken() = userPreference.getToken()


}