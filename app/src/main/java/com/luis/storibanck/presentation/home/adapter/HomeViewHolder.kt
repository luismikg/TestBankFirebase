package com.luis.storibanck.presentation.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luis.storibanck.R
import com.luis.storibanck.databinding.ItemMovementBinding
import com.luis.storibanck.domain.model.MovementInfo


class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemMovementBinding.bind(view)

    fun render(movementInfo: MovementInfo, onItemSelected: (MovementInfo) -> Unit) {
        binding.strTotalMovement.text = movementInfo.total
        binding.strNameMovement.text = movementInfo.name

        Glide.with(binding.root)
            .load(movementInfo.uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_big_bank) // Placeholder image
                    .error(R.drawable.ic_big_bank) // Error image in case of loading failure
            )
            .into(binding.imgIcoShop)

        Glide.with(binding.root)
            .load(
                if (movementInfo.type == 1L) {
                    R.drawable.up_arrow
                } else {
                    R.drawable.down_arrow
                }
            )
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_big_bank) // Placeholder image
                    .error(R.drawable.ic_big_bank) // Error image in case of loading failure
            )
            .into(binding.imgType)

        binding.root.setOnClickListener {
            onItemSelected(movementInfo)
        }
    }
}