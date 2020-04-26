package com.example.yandexautointershipproblem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class SearchRepositoryViewModel : ViewModel() {
    val ans: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val progressVisibility = MutableLiveData(false)

    fun searchRepos(query: String) {
        progressVisibility.value = true
        SearchRepositoriesAPI.retrofitService.searchRepositories(query)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    ans.value = t.message
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    ans.value = response.body()
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
            .baseUrl("https://api.github.com")
            .build()
            .create(SearchRepsInterface::class.java)
    }
}