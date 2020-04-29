package com.example.yandexautointershipproblem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexautointershipproblem.databinding.SearchRepositoryListItemBinding
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.StrictMath.min
import java.net.HttpURLConnection


class SearchRepositoryViewModel : ViewModel() {
    val repositoriesList: MutableLiveData<List<RepositoryRepresentation>>
            by lazy { MutableLiveData<List<RepositoryRepresentation>>() }
    val supportText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val progressVisibility = MutableLiveData(false)

    fun searchAndShowReposAsync(query: String) {
        progressVisibility.value = true
        supportText.value = ""
        val trueQuery = "$query+in:name,description"
        SearchRepositoriesAPI.retrofitService.searchRepositories(trueQuery)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    supportText.value = t.message
                    progressVisibility.value = false
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.code() >= 500) {
                        supportText.value =
                            "An error has occurred on server. Please try one more time. " +
                                    "Error code: " + response.code().toString()
                    } else if (response.code() >= 400) {
                        supportText.value =
                            "Bad request / No internet connection / Too many requests per minute " +
                                    "(10 max). Error code: " + response.code().toString()
                    } else if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.body() == null) {
                            supportText.value = "Null answer returned. Server error"
                        } else {
                            val arrayOfRepositories = JSONObject(response.body()!!)
                                .getJSONArray("items")
                            if (arrayOfRepositories.length() == 0) {
                                supportText.value = "Nothing was found. Try another request"
                            } else {
                                repositoriesList.value =
                                    List(min(50, arrayOfRepositories.length())) { index: Int ->
                                        val curElem = arrayOfRepositories.getJSONObject(index)
                                        RepositoryRepresentation(
                                            curElem.getString("name"),
                                            curElem.getString("description"),
                                            curElem.getJSONObject("owner").getString("login"),
                                            curElem.getString("created_at"),
                                            curElem.getString("language"),
                                            -1
                                        )
                                    }
                            }
                        }
                    } else {
                        supportText.value =
                            "Unknown error. Server behaviour has probably been changed. " +
                                    "Contact to the developer. Error code: " +
                                    response.code().toString()
                    }
                    progressVisibility.value = false
                }
            })
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

class SearchRepositoryAdapter : RecyclerView.Adapter<RepositoryViewHolder>() {
    private var data = listOf<RepositoryRepresentation>()
    fun setData(values: List<RepositoryRepresentation>) {
        data = values
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchRepositoryListItemBinding.inflate(inflater, parent, false)
        return RepositoryViewHolder(binding)
    }
}

class RepositoryViewHolder(private val binding: SearchRepositoryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(repository: RepositoryRepresentation) {
        binding.titleText.text = repository.title
        binding.descriptionText.text = repository.description
        binding.authorText.text = repository.author
        binding.languageText.text = repository.language
        binding.dateText.text = repository.dateOfCreation
    }
}