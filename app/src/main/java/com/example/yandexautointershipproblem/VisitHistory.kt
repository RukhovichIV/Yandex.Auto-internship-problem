package com.example.yandexautointershipproblem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

        val adapter = RepositoryViewAdapter(::onViewItemClick)
        binding.visitHistoryView.adapter = adapter
        val dividerItemDecoration =
            DividerItemDecoration(binding.visitHistoryView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(context?.getDrawable(R.drawable.recycler_view_divider)!!)
        binding.visitHistoryView.addItemDecoration(dividerItemDecoration)
        model.repositoriesList.observe(viewLifecycleOwner, Observer {
            adapter.data = it.toList()
            adapter.notifyDataSetChanged()
        })

        model.supportText.observe(viewLifecycleOwner, Observer<String> {
            binding.supportText.text = it
        })
        model.dataSource = RepoDatabase.getInstance(requireNotNull(this.activity).application).dao
        model.recalculateViews()

        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.bottomAppBar)
        return binding.root
    }

    private fun onViewItemClick(repository: RepositoryRepresentation) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.url))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.action_visitHistory_to_searchRepository)
        return super.onOptionsItemSelected(item)
    }

}
