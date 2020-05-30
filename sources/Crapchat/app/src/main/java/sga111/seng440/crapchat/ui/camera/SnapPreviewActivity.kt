package sga111.seng440.crapchat.ui.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import sga111.seng440.crapchat.R
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
            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show()
            finish()
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

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 12
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }



}
