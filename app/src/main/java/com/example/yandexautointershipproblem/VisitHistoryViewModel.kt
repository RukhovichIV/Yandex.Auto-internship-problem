package com.example.yandexautointershipproblem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexautointershipproblem.storing.RepoDatabaseDao
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation
import kotlinx.coroutines.*

class VisitHistoryViewModel : ViewModel() {
    lateinit var dataSource: RepoDatabaseDao
    var showStarredOnly = false
    private var coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    val repositoriesList: MutableLiveData<MutableList<RepositoryRepresentation>>
            by lazy { MutableLiveData<MutableList<RepositoryRepresentation>>() }

    fun recalculateViews() {
        runBlocking {
            val result = coroutineScope.async {
                if (showStarredOnly) dataSource.getStarredRecords() else
                    dataSource.getAllRecords()
            }
            repositoriesList.value = result.await()
        }
    }

    fun updateItemInDatabase(repository: RepositoryRepresentation) {
        runBlocking {
            val result = coroutineScope.async {
                val repo = dataSource.checkForRecord(repository.author, repository.title)
                if (repo != null) {
                    dataSource.deleteRecord(repo)
                }
                repository.id = dataSource.findMaxId() + 1
                dataSource.insertRecord(repository)
                if (showStarredOnly) dataSource.getStarredRecords() else
                    dataSource.getAllRecords()
            }
            repositoriesList.value = result.await()
        }
    }

    fun removeItemFromDatabase(pos: Int) {
        val repositoryToDelete = repositoriesList.value!![pos]
        coroutineScope.launch {
            dataSource.deleteRecord(repositoryToDelete)
        }
    }

    fun changeStarred(pos: Int) {
        runBlocking {
            with(repositoriesList.value!![pos]) {
                var ok = coroutineScope.launch {
                    dataSource.deleteRecord(this@with)
                }
                ok.join()
                starred = !starred
                ok = coroutineScope.launch {
                    dataSource.insertRecord(this@with)
                }
                ok.join()
            }
        }
    }
}
