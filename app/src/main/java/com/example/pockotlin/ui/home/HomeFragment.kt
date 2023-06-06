package com.example.pockotlin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pockotlin.R
import com.example.pockotlin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_chart)
        }

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nfc)
        }

        binding.button3.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_biometrics)
        }

        binding.button4.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_qrScanner)
        }

        binding.button5.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_bonus)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}