package sga111.seng440.crapchat.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import sga111.seng440.crapchat.R
import java.io.ByteArrayOutputStream
import java.io.File


class SnapPreviewActivity : AppCompatActivity() {

    private lateinit var preview: ImageView
    private lateinit var deleteButton: AppCompatImageButton
    private lateinit var sendButton: AppCompatImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_preview)

        preview = findViewById(R.id.snap_preview)
        sendButton = findViewById(R.id.send_button)
        deleteButton = findViewById(R.id.delete_button)

        val previewUri: String? = intent.getStringExtra("imageUri")

        if (previewUri != null) {
            val uri = Uri.parse(previewUri)
            preview.setImageURI(uri)
        }

        sendButton.setOnClickListener {
//            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show()
//            finish()

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("image/jpeg")

            val file = File(Uri.parse(previewUri).getPath())
            val imageUri = FileProvider.getUriForFile(this, "sga111.seng440.crapchat.provider", file)

            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri) // Could be problem with other applications not having access to this URI
            startActivityForResult(Intent.createChooser(shareIntent, "Share Image"), 33)

        }

        deleteButton.setOnClickListener {
            var fileUri = Uri.parse(previewUri)
            val file = File(fileUri.getPath())
            file.delete()
            if (file.exists()) {
                file.getCanonicalFile().delete()
                if (file.exists()) {
                    applicationContext.deleteFile(file.getName())
                }
            }

            finish()
        }

        // Request storage permissions
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                SnapPreviewActivity.REQUIRED_PERMISSIONS,
                SnapPreviewActivity.REQUEST_CODE_PERMISSIONS
            )
        }

    }

    private fun allPermissionsGranted() = SnapPreviewActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == SnapPreviewActivity.REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 33) {
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 12
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }



}
