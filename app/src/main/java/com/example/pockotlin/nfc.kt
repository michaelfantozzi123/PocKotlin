package com.example.pockotlin

import android.nfc.NfcAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class nfc : Fragment() {

    private var nfcAdapter: NfcAdapter? = null

    companion object {
        fun newInstance() = nfc()
    }

    private lateinit var viewModel: NfcViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())?.let { it }

        return inflater.inflate(R.layout.fragment_nfc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NfcViewModel::class.java)
        // TODO: Use the ViewModel
    }

}