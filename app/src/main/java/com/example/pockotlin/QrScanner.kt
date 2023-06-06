package com.example.pockotlin

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer

class QrScanner : Fragment() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var viewModel: QrScannerViewModel
    private var camera: Camera? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var barcodeScanner: MultiFormatReader? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QrScannerViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permission if not granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseCamera()
    }

    private fun startCamera() {
        val cameraView = view?.findViewById<SurfaceView>(R.id.cameraView)
        surfaceHolder = cameraView?.holder

        surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                initializeCamera()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                releaseCamera()
            }
        })
    }

    private fun initializeCamera() {
        try {
            camera = Camera.open()
            camera?.setDisplayOrientation(90)
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
            camera?.setPreviewCallback { data, camera ->
                val parameters = camera?.parameters
                val width = parameters?.previewSize?.width ?: 0
                val height = parameters?.previewSize?.height ?: 0
                readBarcodeFromCameraData(data, width, height)
            }
        } catch (e: Exception) {
            Log.e("Camera", e.toString())
        }

        barcodeScanner = MultiFormatReader()
    }

    private fun readBarcodeFromCameraData(data: ByteArray, width: Int, height: Int) {
        val rotatedData = IntArray(data.size)
        for (i in data.indices) {
            rotatedData[i] = data[i].toInt() and 0xFF
        }

        val source = RGBLuminanceSource(width, height, rotatedData)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val result: Result = barcodeScanner?.decodeWithState(bitmap) ?: return
            val barcodeData = result.text
            // Handle the barcode data as per your requirements
            Log.d("Barcode", "Scanned barcode: $barcodeData")
        } catch (e: Exception) {
            Log.e("Barcode", e.toString())
        }
    }

    private fun releaseCamera() {
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            // Handle camera permission denial
            // You can display a message or request the permission again
        }
    }
}