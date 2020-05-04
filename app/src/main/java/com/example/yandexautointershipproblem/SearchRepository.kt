package com.example.yandexautointershipproblem

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yandexautointershipproblem.adapters.RepositoryViewAdapter
import com.example.yandexautointershipproblem.databinding.SearchRepositoryFragmentBinding
import com.example.yandexautointershipproblem.storing.RepoDatabase
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation

class SearchRepository : Fragment() {

    private val model: SearchRepositoryViewModel by viewModels()
    private lateinit var binding: SearchRepositoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchRepositoryFragmentBinding.inflate(inflater, container, false)

        binding.searchButton.setOnClickListener {
            hideKeyboard()
            model.searchRepositories(binding.queryInputText.text.toString())
        }

        val adapter = RepositoryViewAdapter(::onViewItemClick)
        binding.searchResultsView.adapter = adapter
        val dividerItemDecoration =
            DividerItemDecoration(binding.searchResultsView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(context?.getDrawable(R.drawable.recycler_view_divider)!!)
        binding.searchResultsView.addItemDecoration(dividerItemDecoration)
        model.repositoriesList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            adapter.notifyDataSetChanged()
        })

        model.supportText.observe(viewLifecycleOwner, Observer<String> {
            binding.supportText.text = it
        })
        model.progressVisibility.observe(viewLifecycleOwner, Observer<Boolean> {
            binding.searchingProgress.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        model.dataSource = RepoDatabase.getInstance(requireNotNull(this.activity).application).dao

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.bottomAppBar)
        return binding.root
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

    }

    private fun onViewItemClick(repository: RepositoryRepresentation) {
        model.addNewItemToDatabase(repository)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.url))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.action_searchRepository_to_visitHistory)
        return super.onOptionsItemSelected(item)
    }
}