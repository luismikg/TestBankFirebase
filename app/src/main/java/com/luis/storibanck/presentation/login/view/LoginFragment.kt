package com.luis.storibanck.presentation.login.view

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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.luis.storibanck.R
import com.luis.storibanck.databinding.FragmentLoginBinding
import com.luis.storibanck.presentation.login.states.LoginState
import com.luis.storibanck.presentation.login.viewModel.LoginViewModel
import com.luis.storibanck.presentation.utils.Errors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initUIState()
    }

    private fun initUi() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.secondaryDark
        )

        binding.apply {
            btnGoRegister.setOnClickListener {
                it.findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment
                )
            }

            btnLogin.setOnClickListener {
                val email = txtUser.text.toString()
                val password = txtPassword.text.toString()

                viewModel.makeLogin(email, password)
            }
        }
    }

    private fun initUIState() {
        viewModel.possibleErrors(
            Errors(
                completeAllField = resources.getString(R.string.complete_all_field),
                credentialsNotValid = resources.getString(R.string.credentials_not_valid)
            )
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        LoginState.Starting -> enableScreen()
                        LoginState.Loading -> disableScreen()
                        LoginState.Success -> successLoading()
                        is LoginState.Error -> {
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

    private fun successLoading() {
        this.findNavController().navigate(R.id.action_loginFragment_to_idPhotoFragment)
    }

    private fun disableScreen() {
        binding.loadingContainer.visibility = View.VISIBLE
    }

    private fun enableScreen() {
        binding.loadingContainer.visibility = View.GONE
    }
}