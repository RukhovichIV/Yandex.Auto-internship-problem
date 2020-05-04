package com.example.yandexautointershipproblem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexautointershipproblem.adapters.RepositoryViewAdapter
import com.example.yandexautointershipproblem.adapters.SwipeToDeleteCallback
import com.example.yandexautointershipproblem.adapters.SwipeToStarCallback
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

        binding.changeViewModeButton.setOnClickListener {
            with(model) {
                if (showStarredOnly) {
                    binding.changeViewModeButton.setImageDrawable(it.context.getDrawable(R.drawable.round_star_half_48))
                } else {
                    binding.changeViewModeButton.setImageDrawable(it.context.getDrawable(R.drawable.round_star_48))
                }
                showStarredOnly = !showStarredOnly
                model.recalculateViews()
            }
        }

        val adapter = RepositoryViewAdapter(::onViewItemClick)
        binding.visitHistoryView.adapter = adapter
        val dividerItemDecoration =
            DividerItemDecoration(binding.visitHistoryView.context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(context?.getDrawable(R.drawable.recycler_view_divider)!!)
        binding.visitHistoryView.addItemDecoration(dividerItemDecoration)

        val swipeLeftHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.removeItemFromDatabase(viewHolder.adapterPosition)
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val swipeRightHandler = object : SwipeToStarCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.changeStarred(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()
            }
        }
        ItemTouchHelper(swipeLeftHandler).attachToRecyclerView(binding.visitHistoryView)
        ItemTouchHelper(swipeRightHandler).attachToRecyclerView(binding.visitHistoryView)

        model.repositoriesList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
            adapter.notifyDataSetChanged()
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
        model.updateItemInDatabase(repository)
        binding.visitHistoryView.adapter!!.notifyDataSetChanged()
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
