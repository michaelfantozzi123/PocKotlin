package com.example.pockotlin

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.pockotlin.databinding.FragmentBiometricsBinding
import com.example.pockotlin.databinding.FragmentHomeBinding
import java.util.concurrent.Executor

class Biometrics : Fragment() {

    private var _binding: FragmentBiometricsBinding? = null
    private val binding get() = _binding!!
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo
    companion object {
        fun newInstance() = Biometrics()
    }

    private lateinit var viewModel: BiometricsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBiometricsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        checkDeviceHasBiometrics(requireContext())

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(requireContext(), "Auth Failed", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(requireContext(), "Auth Success", Toast.LENGTH_LONG).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate User")
            .setSubtitle("Authenticate with biometrics")
            .setNegativeButtonText("Cancel")
            .build()

        binding.biometricButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BiometricsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun checkDeviceHasBiometrics(context: Context) {
        Log.d("APP_TAG", "In method")
        val biometricManager = BiometricManager.from(context)
        Log.d("APP_TAG", "After In method")

        when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("APP_TAG", "App can auth with biometrics")
                binding.canAuthMsg.text = "App can auth with biometrics"
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("APP_TAG", "No Hardware")
                binding.canAuthMsg.text = "No Hardware"
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d("APP_TAG", "Not enrolled biometrics")
            }
        }
    }


}