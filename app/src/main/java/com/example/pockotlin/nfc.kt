package com.example.pockotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class nfc : Fragment() {

    companion object {
        fun newInstance() = nfc()
    }

    private lateinit var viewModel: NfcViewModel
    private var nfcAdapter: NfcAdapter? = null
    private var textViewNfcStatus: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nfc, container, false)
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        textViewNfcStatus = view.findViewById(R.id.textView_nfc_status)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NfcViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent(context, context?.javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))

        try {
            intentFilters[0].addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            e.printStackTrace()
        }

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilters, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    // This method will be called from the Activity when a new NFC intent is received.
    public fun onNfcIntentReceived(message: NdefMessage) {
        textViewNfcStatus?.text = message.toString()
    }
}