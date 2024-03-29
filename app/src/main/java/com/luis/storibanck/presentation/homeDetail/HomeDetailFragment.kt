package com.luis.storibanck.presentation.homeDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luis.storibanck.R
import com.luis.storibanck.databinding.FragmentHomeDetailBinding
import com.luis.storibanck.domain.model.MovementInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDetailFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { arg ->
            val movementInfo = HomeDetailFragmentArgs.fromBundle(arg).movementInfo
            initUI(movementInfo)
        }
    }

    private fun initUI(movementInfo: MovementInfo) {

        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.white
        )

        binding.apply {

            Glide.with(binding.root)
                .load(movementInfo.uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_big_bank)
                        .error(R.drawable.ic_big_bank)
                )
                .into(binding.imgBrand)

            strBrand.text = movementInfo.name
            strTotalPrice.text = movementInfo.total

            mapStore.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=${movementInfo.loc}")
                )
                startActivity(intent)
            }
        }
    }
}