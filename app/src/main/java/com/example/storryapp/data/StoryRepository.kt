package com.example.storryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storryapp.data.network.response.*
import com.example.storryapp.data.network.retrofit.ApiService
import com.example.storryapp.view.database.StoryDatabase
import com.example.storryapp.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {
    private val _responseUpload = MutableLiveData<AddNewStoriesResponse>()
    val responseUpload: LiveData<AddNewStoriesResponse> = _responseUpload
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(name: String, email: String, pass: String): LiveData<ResultResponse<RegisterResponse>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = apiService.register(name, email, pass)
                if (!response.error) {
                    emit(ResultResponse.Success(response))
                } else {
                    Log.e(TAG, "Register Fail: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register Exception: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun login(email: String, pass: String): LiveData<ResultResponse<LoginResult>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = apiService.login(email, pass)
                if (!response.error) {
                    emit(ResultResponse.Success(response.loginResult))
                } else {
                    Log.e(TAG, "Register Fail: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register Exception: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun getStoryMap(token: String): LiveData<ResultResponse<List<ListStoryItem>>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = apiService.getAllStoriesMaps("Bearer $token")
                if (!response.error) {
                    emit(ResultResponse.Success(response.listStory))
                } else {
                    Log.e(TAG, "GetStoryMap Fail: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }

            } catch (e: Exception) {
                Log.e(TAG, "GetStoryMap Exception: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

     fun postStory(
        token: String,
        description: String,
        imgFile: File,
        lat: Float? = null,
        lon: Float? = null
    ){
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestLat = lat.toString().toRequestBody("text/plain".toMediaType())
        val requestLng = lon.toString().toRequestBody("text/plain".toMediaType())
        val requestImage = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imgFile.name,
            requestImage
        )
        _loading.value = true
        val client = apiService.addNewStories("Bearer $token", imageMultipart, requestDescription, requestLat, requestLng)
        client.enqueue(object : Callback<AddNewStoriesResponse> {
            override fun onResponse(call: Call<AddNewStoriesResponse>, response: Response<AddNewStoriesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _responseUpload.value = response.body()
                    _loading.value = false
                }
            }

            override fun onFailure(call: Call<AddNewStoriesResponse>, t: Throwable) {
                _loading.value = false
            }

        })
    }

    fun getPagingStories(token: String): Flow<PagingData<ListStoryItem>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 5,
                    enablePlaceholders = true,
                    initialLoadSize = 5
                ),
                remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getStory()
                }
            ).flow
        }
    }

    companion object {
        private const val TAG = "StoryRepository"
    }
}