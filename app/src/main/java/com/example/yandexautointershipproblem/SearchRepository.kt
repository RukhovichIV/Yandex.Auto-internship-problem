package com.example.yandexautointershipproblem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.yandexautointershipproblem.databinding.SearchRepositoryFragmentBinding


class SearchRepository : Fragment() {

    private val model: SearchRepositoryViewModel by viewModels()
    private lateinit var binding: SearchRepositoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchRepositoryFragmentBinding.inflate(inflater, container, false)
        model.ans.observe(viewLifecycleOwner, Observer<String> { newAns ->
            binding.debugText.text = newAns
        })
        model.progressVisibility.observe(viewLifecycleOwner, Observer<Boolean> { newState ->
            binding.searhingProgress.visibility = if (newState) View.VISIBLE else View.VISIBLE
        })
        binding.searchButton.setOnClickListener {
            model.searchRepos(binding.queryInputText.text.toString())
        }
        return binding.root
    }
}