package com.example.testsearchfragment.adapterimport

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.testsearchfragment.R
import com.example.testsearchfragment.data.ColorWrapper

class SearchAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ColorWrapper>() {
        override fun areItemsTheSame(oldItem: ColorWrapper, newItem: ColorWrapper): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: ColorWrapper, newItem: ColorWrapper): Boolean {
            TODO("not implemented")
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_results_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ColorWrapper>) {
        differ.submitList(list)
    }

    class SearchViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ColorWrapper): Nothing = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            TODO("bind view with data")
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: ColorWrapper)
    }
}
