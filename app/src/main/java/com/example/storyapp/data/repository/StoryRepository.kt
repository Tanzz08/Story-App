package com.example.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.data.ApiService
import com.example.storyapp.data.ResultState
import com.example.storyapp.data.StoryRemoteMediator
import com.example.storyapp.data.UserModel
import com.example.storyapp.data.UserPreference
import com.example.storyapp.data.database.StoryDatabase
import com.example.storyapp.data.database.StoryEntity
import com.example.storyapp.data.response.FileUploadResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.data.response.StoryWithLocation
import com.example.storyapp.utils.EspressoIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
) {
    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "An error occurred"))

        }
    }

    fun login(email: String, password: String) = liveData {
        EspressoIdlingResource.increment()
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            val userModel = UserModel(
                userId = successResponse.loginResult.userId,
                name = successResponse.loginResult.name,
                token = successResponse.loginResult.token,
                email = email,
                password = password
            )
            userPreference.saveToken(userModel)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } finally {
            EspressoIdlingResource.decrement()
        }

    }

    fun getStories(): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, userPreference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).flow
    }

    fun getStoriesWithLocation(): LiveData<ResultState<List<StoryWithLocation>>> = liveData {
        emit(ResultState.Loading)
        try {
            val token = userPreference.getToken().first()
            val stories = apiService.getStoriesWithLocation("Bearer $token")
            val storiesWithLocation = stories.listStory.map { story ->
                StoryWithLocation(story, story.lat, story.lon)
            }
            emit(ResultState.Success(storiesWithLocation))

        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    suspend fun uploadImage(
        token: String,
        file: File,
        description: String,
        lat: Float,
        lon: Float
    ): FileUploadResponse {
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        return apiService.uploadImage(
            "Bearer $token",
            multipartBody,
            descriptionRequestBody,
            lat,
            lon
        )
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference, database: StoryDatabase) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref, database)
            }.also { instance = it }
    }
}