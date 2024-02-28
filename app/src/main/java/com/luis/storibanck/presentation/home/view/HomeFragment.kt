package com.luis.storibanck.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.luis.storibanck.R
import com.luis.storibanck.databinding.FragmentHomeBinding
import com.luis.storibanck.domain.model.MovementInfo
import com.luis.storibanck.presentation.home.adapter.HomeAdapter
import com.luis.storibanck.presentation.home.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapterMovement: HomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableScreen()

        initUI()
        initMovementList()
        initObservers()
        viewModel.fetchMovement()
    }

    private fun initUI() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            R.color.primary
        )

        binding.apply {
            switchTotal.isChecked = true
            switchTotal.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    strTotal.visibility = View.VISIBLE
                    strTotalToday.visibility = View.VISIBLE
                    strTodayLabel.visibility = View.VISIBLE
                } else {
                    strTotal.visibility = View.INVISIBLE
                    strTotalToday.visibility = View.INVISIBLE
                    strTodayLabel.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initMovementList() {
        adapterMovement = HomeAdapter(onItemSelected = {
            clickItem(it)
        })
        binding.apply {
            rvMovement.layoutManager = LinearLayoutManager(requireContext())
            rvMovement.adapter = adapterMovement
        }
    }

    private fun initObservers() {
        viewModel.liveDataMovements.observe(viewLifecycleOwner) {
            successMovement(it)
        }
        viewModel.liveDataHead.observe(viewLifecycleOwner) {
            successHead(it.total, it.today)
        }
    }

    private fun successHead(total: String, today: String) {
        binding.strTotal.text = total
        binding.strTotalToday.text = today
    }

    private fun successMovement(movementList: List<MovementInfo>) {
        binding.loadingContainer.visibility = View.GONE
        adapterMovement.updateList(movementList)
    }

    private fun disableScreen() {
        binding.loadingContainer.visibility = View.VISIBLE
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    private fun clickItem(movementInfo: MovementInfo) {
        if (this.findNavController().currentDestination?.id == R.id.homeFragment) {
            //this.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            showSnackBar("${movementInfo.total} - ${movementInfo.name}")
        }
    }
}