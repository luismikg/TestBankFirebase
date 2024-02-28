package com.luis.storibanck.presentation.register.view

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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.luis.storibanck.databinding.FragmentRegisterBinding
import com.luis.storibanck.R
import com.luis.storibanck.presentation.utils.Errors
import com.luis.storibanck.presentation.register.states.RegisterState
import com.luis.storibanck.presentation.register.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableScreen()
        initUi()
        initUIState()
    }

    private fun initUi() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.primaryDark
        )

        binding.apply {
            btnGoLogin.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            btnRegister.setOnClickListener {
                val name = txtName.text.toString()
                val email = txtEmail.text.toString()
                val password = txtPasswordOne.text.toString()
                val passwordConfirmation = txtPasswordTwo.text.toString()

                viewModel.register(name, email, password, passwordConfirmation)
            }
        }
    }

    private fun initUIState() {
        viewModel.possibleErrors(
            Errors(
                resources.getString(R.string.complete_all_field),
                resources.getString(R.string.password_not_equals),
                resources.getString(R.string.weak_password),
                resources.getString(R.string.credentials_not_valid),
                resources.getString(R.string.user_already_exists)
            )
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        RegisterState.Starting -> enableScreen()
                        RegisterState.Loading -> disableScreen()
                        is RegisterState.Success -> successRegister()
                        is RegisterState.Error -> {
                            enableScreen()
                            showSnackBar(state.error)
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    private fun successRegister() {
        if (this.findNavController().currentDestination?.id == R.id.registerFragment) {
            this.findNavController()
                .navigate(RegisterFragmentDirections.actionRegisterFragmentToIdPhotoFragment())
        }
    }

    private fun disableScreen() {
        binding.loadingContainer.visibility = View.VISIBLE
    }

    private fun enableScreen() {
        binding.loadingContainer.visibility = View.GONE
    }
}