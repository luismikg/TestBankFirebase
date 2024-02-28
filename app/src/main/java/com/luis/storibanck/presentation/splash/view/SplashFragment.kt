package com.luis.storibanck.presentation.splash.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luis.storibanck.R
import com.luis.storibanck.databinding.FragmentSplashBinding
import com.luis.storibanck.presentation.splash.states.SplashState
import com.luis.storibanck.presentation.splash.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initUIState()
        viewModel.startApplicacionCount()
    }

    private fun initUI() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.white_splash
        )
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        SplashState.StartApplication -> {
                            startApplication()
                        }

                        SplashState.Starting -> {}
                    }
                }
            }
        }
    }

    private fun startApplication() {
        if (this.findNavController().currentDestination?.id == R.id.splashFragment) {
            this.findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            )
        }
    }
}