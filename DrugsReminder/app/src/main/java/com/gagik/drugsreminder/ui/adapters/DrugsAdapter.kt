package com.gagik.drugsreminder.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gagik.drugsreminder.common.database.entities.DrugsEntity
import com.gagik.drugsreminder.databinding.ItemViewDrugsBinding
import com.gagik.drugsreminder.ui.fragments.DrugsListeners

class DrugsAdapter(private val itemClickListener: DrugsListeners) :
    ListAdapter<DrugsEntity, DrugsAdapter.DrugsViewHolder>(DrugsDiffUtils()) {

    private var originalList: List<DrugsEntity> = emptyList()

    inner class DrugsViewHolder(private val binding: ItemViewDrugsBinding) : ViewHolder(binding.root) {

        fun bind(item: DrugsEntity) {
            binding.drugName.text = item.name
            binding.activeFactor.text = item.activeFactor
            binding.moreBtn.setOnClickListener {
                itemClickListener.onDragDetailsClick(item)
            }

            binding.deleteButton.setOnClickListener {
                itemClickListener.onDeleteItem(item)
            }

            binding.root.setOnClickListener {
                itemClickListener.onDragItemClick(item)
            }
        }
    }

    class DrugsDiffUtils : DiffUtil.ItemCallback<DrugsEntity>() {

        override fun areItemsTheSame(oldItem: DrugsEntity, newItem: DrugsEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrugsEntity, newItem: DrugsEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugsViewHolder {
        return DrugsViewHolder(
            ItemViewDrugsBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: DrugsViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    fun submitOriginalList(list: List<DrugsEntity>) {
        originalList = list
        filterList("") // Show the full list initially
    }

    fun filterList(query: String) {
        val lowercaseQuery = query.lowercase()
        val filteredList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.name?.lowercase()?.contains(lowercaseQuery) == true
            }
        }
        submitList(filteredList)
    }
}