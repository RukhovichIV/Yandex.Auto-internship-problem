package com.example.yandexautointershipproblem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.yandexautointershipproblem.databinding.SearchRepositoryFragmentBinding
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation


class SearchRepository : Fragment() {

    private val model: SearchRepositoryViewModel by viewModels()
    private lateinit var binding: SearchRepositoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchRepositoryFragmentBinding.inflate(inflater, container, false)
        val adapter = SearchRepositoryAdapter()
        binding.searchResultsView.adapter = adapter
        model.repositoriesList.observe(viewLifecycleOwner, Observer<List<RepositoryRepresentation>>{
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
        model.supportText.observe(viewLifecycleOwner, Observer<String> {
            binding.debugText.text = it
        })
        model.progressVisibility.observe(viewLifecycleOwner, Observer<Boolean> {
            binding.searhingProgress.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        binding.searchButton.setOnClickListener {
            hideKeyboard()
            model.searchAndShowReposAsync(binding.queryInputText.text.toString())
        }
        return binding.root
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

    }
}