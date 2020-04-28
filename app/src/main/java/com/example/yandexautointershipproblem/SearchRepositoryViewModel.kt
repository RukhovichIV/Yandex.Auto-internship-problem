package com.example.yandexautointershipproblem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.HttpURLConnection

class SearchRepositoryViewModel : ViewModel() {
    val ans: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val progressVisibility = MutableLiveData(false)

    fun searchRepos(query: String) {
        progressVisibility.value = true
//        CoroutineScope(Job() + Dispatchers.Main).launch {
//            var getPropertiesDeferred =
//                SearchRepositoriesAPI.retrofitService.searchRepositoriesAsync(query)
//            try {
//                var listResult = getPropertiesDeferred.await()
//                if (response.code() >= 500) {
//                    ans.value =
//                        "An error has occurred on server. Please try one more time. " +
//                                "Error code: " + response.code().toString()
//                } else if (response.code() >= 400) {
//                    ans.value =
//                        "Bad request / No internet connection / Too many requests per minute " +
//                                "(10 max). Error code: " + response.code().toString()
//                } else if (response.code() == HttpURLConnection.HTTP_OK) {
//                    if (response.body() == null) {
//                        ans.value = "Null answer returned. Server error"
//                    } else {
//                        val jsonBody = JSONObject(listResult)
//                        if (!jsonBody.getBoolean("incomplete_results")) {
//                            val arrayOfRepositories = jsonBody.getJSONArray("items")
//                            ans.value = "OK"
//                        }
//                    }
//                } else {
//                    ans.value =
//                        "Unknown error. Server behaviour has probably been changed. " +
//                                "Contact to the developer. Error code: " +
//                                response.code().toString()
//                }
//            } catch (e: Exception) {
//                ans.value = e.message
//            }
//        }
        SearchRepositoriesAPI.retrofitService.searchRepositories(query)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    ans.value = t.message
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() >= 500) {
                        ans.value =
                            "An error has occurred on server. Please try one more time. " +
                                    "Error code: " + response.code().toString()
                    } else if (response.code() >= 400) {
                        ans.value =
                            "Bad request / No internet connection / Too many requests per minute " +
                                    "(10 max). Error code: " + response.code().toString()
                    } else if (response.code() == HttpURLConnection.HTTP_OK) {
                        if(response.body() == null){
                            ans.value = "Null answer returned. Server error"
                        } else {
                            val jsonBody = JSONObject(response.body()!!)
                            if (!jsonBody.getBoolean("incomplete_results")) {
                                val arrayOfRepositories = jsonBody.getJSONArray("items")
                                ans.value = "OK"
                            }
                        }
                    } else {
                        ans.value = "Unknown error. Server behaviour has probably been changed. " +
                                "Contact to the developer. Error code: " +
                                response.code().toString()
                    }
                }
            })
        progressVisibility.value = false
    }
}

interface SearchRepsInterface {
    @GET("search/repositories")
    fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc"
    ): Call<String>
}

object SearchRepositoriesAPI {
    val retrofitService: SearchRepsInterface by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.github.com")
            .build()
            .create(SearchRepsInterface::class.java)
    }
}

class RepositoryRepresentation(
    title: String,
    description: String,
    author: String,
    dateOfCreation: String,
    language: String
)