package com.luis.storibanck.presentation.congrats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.luis.storibanck.R
import com.luis.storibanck.databinding.FragmentCongratsBinding

class CongratsFragment : Fragment() {

    private lateinit var binding: FragmentCongratsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCongratsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.primary
        )

        binding.btnContinue.setOnClickListener {
            clickNext()
        }
    }

    private fun clickNext() {
        if (this.findNavController().currentDestination?.id == R.id.congratsFragment) {
            this.findNavController()
                .navigate(CongratsFragmentDirections.actionCongratsFragmentToHomeFragment())
        }
    }
}