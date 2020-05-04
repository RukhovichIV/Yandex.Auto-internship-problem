package com.example.yandexautointernshipproblem.internet

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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

fun searchRepositoriesAsync(
    query: String,
    failureCallback: (Throwable) -> Unit,
    successCallback: (Response<String>) -> Unit
) {
    val trueQuery = "$query+in:name,description"
    SearchRepositoriesAPI.retrofitService.searchRepositories(trueQuery)
        .enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                failureCallback(t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                successCallback(response)
            }
        })
}