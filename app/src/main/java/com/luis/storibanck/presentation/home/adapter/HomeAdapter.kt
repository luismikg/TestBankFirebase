package com.luis.storibanck.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luis.storibanck.R
import com.luis.storibanck.domain.model.MovementInfo

class HomeAdapter(
    private var movementItems: List<MovementInfo> = emptyList(),
    private val onItemSelected: (MovementInfo) -> Unit
) : RecyclerView.Adapter<HomeViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<MovementInfo>) {
        movementItems = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movement, parent, false)
        )
    }

    override fun getItemCount() = movementItems.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.render(movementItems[position], onItemSelected)
    }
}