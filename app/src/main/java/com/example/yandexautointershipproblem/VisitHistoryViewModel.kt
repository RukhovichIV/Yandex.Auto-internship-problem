package com.example.yandexautointershipproblem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexautointershipproblem.storing.RepoDatabaseDao
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation

class VisitHistoryViewModel : ViewModel() {
    lateinit var historyDataSource: RepoDatabaseDao
    val repositoriesList: MutableLiveData<LinkedHashSet<RepositoryRepresentation>>
            by lazy { MutableLiveData<LinkedHashSet<RepositoryRepresentation>>() }
    val supportText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private var maxId = -1

    fun recalculateViews() {
        repositoriesList.value!!.clear()
        repositoriesList.value!!.addAll(historyDataSource.getAllRecords())
        maxId = repositoriesList.value!!.maxBy { it.id }?.id ?: -1
    }

    fun addNewItem(repository: RepositoryRepresentation) {
        if (historyDataSource.checkForRecord(repository.author, repository.title).size == 1) {
            historyDataSource.deleteRecord(repository)
        }
        val wasInList = repositoriesList.value!!.remove(repository)
        repository.id = ++maxId
        if (wasInList) repositoriesList.value!!.add(repository)
        historyDataSource.insertRecord(repository)
    }

    fun removeItem(repository: RepositoryRepresentation) {
        if (historyDataSource.checkForRecord(repository.author, repository.title).size == 1) {
            historyDataSource.deleteRecord(repository)
        }
        repositoriesList.value!!.remove(repository)
    }
}
