package com.example.yandexautointershipproblem.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexautointershipproblem.databinding.RepositoryListItemBinding
import com.example.yandexautointershipproblem.storing.RepositoryRepresentation

class RepositoryViewAdapter(private val clickListener: (RepositoryRepresentation) -> Unit) :
    RecyclerView.Adapter<RepositoryViewAdapter.ViewHolder>() {
    var data = listOf<RepositoryRepresentation>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], clickListener, position+1 == data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepositoryListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: RepositoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            repository: RepositoryRepresentation,
            clickListener: (RepositoryRepresentation) -> Unit,
            isLast: Boolean
        ) {
            if (isLast){
                val params = binding.repoItemLayout.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 150
                binding.repoItemLayout.layoutParams = params
            }else {
                val params = binding.repoItemLayout.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 0
                binding.repoItemLayout.layoutParams = params

                binding.titleText.text = repository.title
                binding.descriptionText.text = repository.description
                binding.authorText.text = repository.author
                binding.languageText.text = repository.language
                binding.dateText.text = repository.dateOfCreation
                binding.repoItemLayout.setOnClickListener { clickListener(repository) }
            }
        }
    }
}
