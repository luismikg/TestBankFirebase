package com.luis.storibanck.presentation.idPhoto.view

import android.animation.Animator
import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.luis.storibanck.R
import com.luis.storibanck.databinding.FragmentIdPhotoBinding
import com.luis.storibanck.presentation.idPhoto.states.IdPhotoState
import com.luis.storibanck.presentation.idPhoto.viewModel.IdPhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class IdPhotoFragment : Fragment() {

    private lateinit var binding: FragmentIdPhotoBinding
    private val viewModel: IdPhotoViewModel by viewModels()

    private var imageCapture: ImageCapture? = null
    private val cameraPermission = android.Manifest.permission.CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIdPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(cameraPermission)
        }
    }

    private fun checkCameraPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            requireContext(),
            cameraPermission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isOk ->
        if (isOk) {
            startCamera()
        } else {
            showSnackBar(resources.getText(R.string.camera_needs).toString())
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview
                .Builder()
                .build()
                .also { it.setSurfaceProvider(binding.viewIdCard.surfaceProvider) }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.i("Error", "${e.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))

        initUi()
        initUIState()
    }

    private fun initUi() {

        binding.apply {
            idScanAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    btnTakePhoto.setOnClickListener {
                        takePhoto()
                    }
                }
            })
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        IdPhotoState.Starting -> enableScreen()
                        IdPhotoState.Loading -> disableScreen()
                        is IdPhotoState.Success -> successPhotoSaved()
                        is IdPhotoState.Error -> {
                            enableScreen()
                            showSnackBar(state.error)
                        }
                    }
                }
            }
        }
    }

    private fun takePhoto() {
        val name = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {}

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewModel.savePhoto(output.savedUri)
                }
            }

        )
    }

    private fun successPhotoSaved() {
        binding.apply {
            successAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    next()
                }
            })
            successAnimation.playAnimation()
        }
    }

    private fun enableScreen() {
        binding.apply {
            btnTakePhoto.visibility = View.VISIBLE
            binding.loadingContainer.visibility = View.GONE
        }
    }

    private fun disableScreen() {
        binding.apply {
            btnTakePhoto.visibility = View.GONE
            loadingContainer.visibility = View.VISIBLE
        }
    }

    private fun next() {
        if (this.findNavController().currentDestination?.id == R.id.idPhotoFragment) {
            this.findNavController().navigate(R.id.action_idPhotoFragment_to_congratsFragment)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, BaseTransientBottomBar.LENGTH_SHORT).show()
    }
}