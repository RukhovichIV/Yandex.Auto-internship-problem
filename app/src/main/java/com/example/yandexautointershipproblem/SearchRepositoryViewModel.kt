package com.example.yandexautointershipproblem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexautointershipproblem.internet.searchRepositoriesAsync
import com.example.yandexautointershipproblem.storing.RepoDatabaseDao
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.net.HttpURLConnection

class SearchRepositoryViewModel : ViewModel() {
    lateinit var dataSource: RepoDatabaseDao
    private var coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    val repositoriesList: MutableLiveData<List<RepositoryRepresentation>>
            by lazy { MutableLiveData<List<RepositoryRepresentation>>() }
    val supportText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val progressVisibility = MutableLiveData(false)

    fun searchRepositories(query: String) {
        progressVisibility.value = true
        supportText.value = ""
        repositoriesList.value = listOf()
        searchRepositoriesAsync(query, ::failureCallback, ::successCallback)
    }

    fun addNewItemToDatabase(repository: RepositoryRepresentation) {
        coroutineScope.launch {
            dataSource.deleteRecord(repository)
            repository.id = dataSource.findMaxId() + 1
            dataSource.insertRecord(repository)
        }
    }

    private fun failureCallback(t: Throwable) {
        supportText.value = t.message
        progressVisibility.value = false
    }

    private fun successCallback(response: Response<String>) {
        if (response.code() >= 500) {
            supportText.value = "An error has occurred on server. Please try one more time. " +
                    "Error code: " + response.code().toString()
        } else if (response.code() >= 400) {
            supportText.value = "Bad request / No internet connection / Too many requests per " +
                    "minute (10 max). Error code: " + response.code().toString()
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
                        List(StrictMath.min(50, arrayOfRepositories.length())) { index: Int ->
                            val curElem = arrayOfRepositories.getJSONObject(index)
                            RepositoryRepresentation(
                                curElem.getString("name"),
                                curElem.getString("description").checkDesc(),
                                curElem.getJSONObject("owner").getString("login"),
                                curElem.getString("created_at").normalizeDate(),
                                curElem.getString("language").checkLang(),
                                curElem.getString("html_url"),
                                curElem.getInt("id"),
                                false
                            )
                        }
                }
            }
        } else {
            supportText.value = "Unknown error. Server behaviour has probably been changed. " +
                    "Contact to the developer. Error code: " + response.code().toString()
        }
        progressVisibility.value = false
    }
}

fun String.checkDesc(): String {
    return if (this == "null") "No description was provided" else this
}

fun String.checkLang(): String {
    return if (this == "null") "/unknown/" else this
}

fun String.normalizeDate(): String {
    return (if (this[8] != '0') this[8].toString() else "") +
            this[9] + "/" +
            (if (this[5] != '0') this[5].toString() else "") +
            this[6] + "/" + this[0] + this[1] + this[2] + this[3]
}