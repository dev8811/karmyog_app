package com.example.myapplication.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class WaitingListAdapter(private val waitingList: List<WaitingListItem>) :
    RecyclerView.Adapter<WaitingListAdapter.WaitingListViewHolder>() {

    class WaitingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewDetails: TextView = itemView.findViewById(R.id.textViewDetails)
        val textViewidex: TextView = itemView.findViewById(R.id.textViewIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaitingListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_waiting_list, parent, false)
        return WaitingListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WaitingListViewHolder, position: Int) {
        val currentItem = waitingList[position]
        holder.textViewName.text = currentItem.name
        holder.textViewDetails.text = currentItem.details
        val index: Int = position + 1
        holder.textViewidex.text = index.toString()

    }

    override fun getItemCount() = waitingList.size
}
