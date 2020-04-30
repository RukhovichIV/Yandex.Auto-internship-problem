package com.example.yandexautointershipproblem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexautointershipproblem.storing.RepoDatabaseDao
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation
import kotlinx.coroutines.*

class VisitHistoryViewModel : ViewModel() {
    lateinit var dataSource: RepoDatabaseDao
    private var coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    val repositoriesList: MutableLiveData<LinkedHashSet<RepositoryRepresentation>>
            by lazy { MutableLiveData<LinkedHashSet<RepositoryRepresentation>>() }
    val supportText: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun recalculateViews() {
        runBlocking {
            val result = coroutineScope.async {
                dataSource.getAllRecords()
            }
            repositoriesList.value = LinkedHashSet(result.await())
        }
    }

    fun removeItemFromDatabase(repository: RepositoryRepresentation) {
        coroutineScope.launch {
            dataSource.deleteRecord(repository)
        }
    }
}
