package com.utn.MascotApp.fragments


import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.utn.MascotApp.*
import com.utn.MascotApp.R.drawable.*
import com.utn.MascotApp.R.color.*
import com.google.firebase.firestore.DocumentSnapshot
import com.utn.MascotApp.databinding.FragmentMapsBinding
import java.util.*
import com.google.firebase.firestore.DocumentChange
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat


class MapsFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener {

    private var locationPermissionGranted = false
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-34.62718052757213, -58.45845530464802)
    private val foundPetIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), blue_grey_900)
        BitmapHelper.vectorToBitmap(requireContext(), pet_footprint, color)
    }

    private val lostPetIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), quantum_error_light)
        BitmapHelper.vectorToBitmap(requireContext(), pet_footprint, color)
    }


    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "Granted", Toast.LENGTH_LONG)
                locationPermissionGranted = true
            } else {
                Toast.makeText(context, "The map requires permission!", Toast.LENGTH_LONG)
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onResume() {
        super.onResume()
        this.map?.let { it1 -> addMarkers(it1) }
        getDeviceLocation()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
                as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        updateLocationUI()
        getDeviceLocation()
        listenToDiffs()

        binding.miLocation.setOnClickListener{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            updateLocationUI()
            getDeviceLocation()
            this.map?.let { it1 -> addMarkers(it1) }
        }


    }

    private fun listenToDiffs() {
        // [START listen_diffs]
        val db = Firebase.firestore
        db.collection("publications")
            .whereEqualTo("state", "CA")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> map?.let { addMarkers(it) }
                        DocumentChange.Type.MODIFIED -> map?.let { addMarkers(it) }
                        DocumentChange.Type.REMOVED -> map?.let { addMarkers(it) }
                    }
                }
            }
        // [END listen_diffs]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }


    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // [START_EXCLUDE]
        // [START map_current_place_set_info_window_adapter]
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        /*this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    findViewById<FrameLayout>(R.id.map), false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })*/
         map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.style_json));

        map.setOnInfoWindowClickListener(this)
        // Prompt the user for permission.
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
        addMarkers(map)
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun addMarkers(googleMap: GoogleMap) {
        // Set custom info window adapter
        googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
        val db = Firebase.firestore
        db.collection("publications")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val lat = document.data["geolocation"] as GeoPoint
                    var petIcon: BitmapDescriptor
                    if (document.data["type"] == "found")  petIcon = this.foundPetIcon
                    else  petIcon = this.lostPetIcon
                    
                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .title("${document.data["name"]}")
                            .position( LatLng(lat.latitude, lat.longitude))
                            .icon(petIcon)
                    )
                    marker?.tag = document.data["imagePath"]
                    println("Publication: ${document?.data}")
                }
            }

    }



    private fun showError(){
        Toast.makeText(context, "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 16
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }

    override fun onInfoWindowClick(marker: Marker) {
        val db = Firebase.firestore
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("publication-images")
        marker.tag?.toString()?.let { Log.e("ivo", it) }
        db.collection("publications").whereEqualTo("imagePath", marker.tag?.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var map: Map<String, Any> = document.data
                    marker.tag?.toString()?.let { Log.e("ivo", document.data.toString()) }
                    imagesRef.child(map.get("imagePath").toString()).downloadUrl.addOnSuccessListener {

                        var publications = Publications(
                        address = map["address"].toString(),
                        color = map["color"].toString(),
                        imagePath = it.toString(),
                        description = map["description"].toString(),
                        type = map["type"].toString(),
                        breed = map["breed"].toString(),
                        createdAt = map["createdAt"] as Timestamp,
                        lastSeen = map["lastSeen"] as Timestamp,
                        size = map["size"].toString(),
                        createdBy = map["createdBy"].toString(),
                        species = map["species"].toString(),
                        name = map["name"].toString(),
                        geolocation = map["geolocation"] as GeoPoint,
                        age = map["age"].toString(),
                        sex = map["sex"].toString()
                    )
                        val sdf = SimpleDateFormat("dd/MM/yyyy")
                        var dateLastSeen = sdf.format(publications.lastSeen.toDate())
                    val action = MainMenuFragmentDirections.actionMainMenuFragmentToMascotInfoFragment(
                        publications.imagePath, publications.name,
                        publications.description, publications.age.toInt(), publications.sex,
                        publications.color, publications.breed, dateLastSeen,
                        publications.address, publications.createdBy,
                        "MascotasVistas"
                    )
                    findNavController().navigate(action)
                }
                    break
                }
            }
    }

}

