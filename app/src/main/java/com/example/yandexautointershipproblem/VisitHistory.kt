package com.example.yandexautointershipproblem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.yandexautointershipproblem.adapters.RepositoryViewAdapter
import com.example.yandexautointershipproblem.databinding.VisitHistoryFragmentBinding
import com.example.yandexautointershipproblem.storing.RepoDatabase
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation


class VisitHistory : Fragment() {

    private val model: VisitHistoryViewModel by viewModels()
    private lateinit var binding: VisitHistoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VisitHistoryFragmentBinding.inflate(inflater, container, false)
        val dataSource = RepoDatabase.getInstance(requireNotNull(this.activity).application).dao
        model.historyDataSource = dataSource
        val adapter = RepositoryViewAdapter(::onViewItemClick)
        binding.visitHistoryView.adapter = adapter
        model.repositoriesList.observe(viewLifecycleOwner, Observer {
            adapter.data = it.toList()
            adapter.notifyDataSetChanged()
        })
        model.supportText.observe(viewLifecycleOwner, Observer<String> {
            binding.supportText.text = it
        })
        model.recalculateViews()
        return binding.root
    }

    private fun onViewItemClick(repository: RepositoryRepresentation) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.url))
        startActivity(intent)
    }

}
