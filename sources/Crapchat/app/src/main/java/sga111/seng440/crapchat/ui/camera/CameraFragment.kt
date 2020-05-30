package sga111.seng440.crapchat.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_camera.*
import sga111.seng440.crapchat.R
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var cameraViewModel: CameraViewModel
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraCaptureButton: AppCompatImageButton
    private lateinit var cameraFlipButton: AppCompatImageButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                activity as Activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Setup the listener for take photo button
        cameraCaptureButton = view.findViewById(R.id.camera_capture_button)
        cameraCaptureButton.setOnClickListener { takePhoto() }

        // Setup the listener for the flip camera button
        cameraFlipButton = view.findViewById(R.id.camera_flip_button)
        cameraFlipButton.setOnClickListener { flipCamera() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun flipCamera() {

        // Spin the flip camera icon
        val cameraFlipAnimation = AnimationUtils.loadAnimation(context!!, R.anim.camera_flip_animation);
        cameraFlipButton.startAnimation(cameraFlipAnimation)

        lensFacing = if (CameraSelector.LENS_FACING_BACK == lensFacing) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context!!)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            try {
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)
                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context!!))

    }

    private fun takePhoto() {
        // Animate the photo capture button
        val takePhotoAnimation = AnimationUtils.loadAnimation(context!!, R.anim.take_picture_animation);
        cameraCaptureButton.startAnimation(takePhotoAnimation)

        // Take photo

        Log.d(TAG, "Before image capture return")

        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.ENGLISH)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        Log.d(TAG, "Before taking picture")

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    val previewIntent = Intent(context!!, SnapPreviewActivity::class.java)
                    previewIntent.putExtra("imageUri", savedUri.toString())
                    previewIntent.putExtra("name", photoFile.name)
                    startActivity(previewIntent)
                }
            }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context!!, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        return File(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), resources.getString(R.string.app_name))}

    companion object {
        private const val TAG = "Crapchat"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}
