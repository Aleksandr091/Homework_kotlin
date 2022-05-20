package ru.chistov.homework.lesson10

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.chistov.homework.R
import ru.chistov.homework.databinding.FragmentMapsBinding
import ru.chistov.homework.databinding.FragmentMapsMainBinding
import ru.chistov.homework.databinding.FragmentWeatherListBinding
import ru.chistov.homework.view.weatherList.WeatherListAdapter

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsMainBinding? = null
    private val binding: FragmentMapsMainBinding
        get() {
            return _binding!!
        }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map=googleMap
        val moscow = LatLng(55.0, 37.0)
        googleMap.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(moscow))
        map.setOnMapLongClickListener {
            addMarkerToArray(it)
            drawLine()
        }
        map.uiSettings.isZoomControlsEnabled = true
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.uiSettings.isMyLocationButtonEnabled=true
            map.isMyLocationEnabled = true

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )!!
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }
    private fun drawLine(){
        var previousBefore: Marker? = null
        markers.forEach { current->
            previousBefore?.let{  previous->
                map.addPolyline(
                    PolylineOptions().add(previous.position,current.position)
                        .color(Color.RED)
                        .width(5f))
            }
            previousBefore = current
        }
    }
}