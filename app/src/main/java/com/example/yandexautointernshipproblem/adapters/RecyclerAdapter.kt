package com.example.yandexautointernshipproblem.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexautointernshipproblem.R
import com.example.yandexautointernshipproblem.databinding.RepositoryListItemBinding
import com.example.yandexautointernshipproblem.storing.RepositoryRepresentation

class RepositoryViewAdapter(private val clickListener: (RepositoryRepresentation) -> Unit) :
    RecyclerView.Adapter<RepositoryViewAdapter.ViewHolder>() {
    var data = mutableListOf<RepositoryRepresentation>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepositoryListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    fun removeAt(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(private val binding: RepositoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            repository: RepositoryRepresentation,
            clickListener: (RepositoryRepresentation) -> Unit
        ) {
            with(binding) {
                titleText.text = repository.title
                descriptionText.text = repository.description
                authorText.text = repository.author
                languageText.text = repository.language
                dateText.text = repository.dateOfCreation
                repoItemLayout.setOnClickListener { clickListener(repository) }
                repoItemLayout.background =
                    if (repository.starred) ContextCompat.getDrawable(
                        repoItemLayout.context,
                        R.color.secondaryLightColor
                    ) else ColorDrawable(Color.TRANSPARENT)
            }
        }
    }
}
