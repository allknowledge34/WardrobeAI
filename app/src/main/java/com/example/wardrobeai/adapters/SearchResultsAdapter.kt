package com.example.wardrobeai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wardrobeai.R
import com.example.wardrobeai.models.clothing.ClosetOrganiserModel
import com.example.wardrobeai.models.outfit.OutfitModel

class SearchResultsAdapter(
    private val onItemClick: (Any) -> Unit
) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    // List of search result items (can be either ClosetOrganiserModel or OutfitModel)
    private val items = mutableListOf<Any>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.searchItemTitle)
        val type: TextView = view.findViewById(R.id.searchItemType)
    }

    fun updateList(newItems: List<Any>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is ClosetOrganiserModel -> {
                holder.title.text = item.title
                holder.type.text = "Clothing"
            }
            is OutfitModel -> {
                holder.title.text = item.title
                holder.type.text = "Outfit"
            }
        }
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size
}