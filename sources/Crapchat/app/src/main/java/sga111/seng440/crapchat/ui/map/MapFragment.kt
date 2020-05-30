package sga111.seng440.crapchat.ui.map

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import sga111.seng440.crapchat.R
import sga111.seng440.crapchat.ui.camera.CameraFragment

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var map: GoogleMap

    private val hasLocationPermissions
        get() = ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!hasLocationPermissions) {
            ActivityCompat.requestPermissions(
                activity as Activity,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        promptForGps()


        return view
    }

    private fun promptForGps() {
        val locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(context).apply {
                setMessage("GPS not currently enabled, please enable it to use the SnapMap!")
                setPositiveButton("Settings") {_, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                setNegativeButton("Cancel") {_, _ ->}
                show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (hasLocationPermissions) {
            map.isMyLocationEnabled = true
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
