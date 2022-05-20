package ru.chistov.homework.view.weatherList

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.chistov.homework.R
import ru.chistov.homework.databinding.FragmentWeatherListBinding
import ru.chistov.homework.repository.City
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.utils.KEY_BUNDLE_WEATHER
import ru.chistov.homework.utils.KEY_SP_FILE_NAME_1
import ru.chistov.homework.utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import ru.chistov.homework.utils.REQUEST_CODE_GPS
import ru.chistov.homework.view.details.DetailsFragment
import ru.chistov.homework.viewmodel.AppState
import ru.chistov.homework.viewmodel.MainViewModel


class WeatherListFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }
    private val adapter = WeatherListAdapter(this)


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root

    }


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        getSP()
        changeWeatherDataSetImage()
        if (isRussian) {
            viewModel.getWeatherRussian()
        } else {
            viewModel.getWeatherWorld()
        }
        setFabCity()
        val observer = Observer<AppState> { data -> renderData(data) }
        setupFabLocation()
        viewModel.getData().observe(viewLifecycleOwner, observer)


    }
    private fun setupFabLocation() {
        binding.FABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        // а есть ли разрешение?
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // важно написать убедительную просьбу
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }


    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_GPS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_GPS) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun getAddressByLocation(location: Location){
        val geocoder = Geocoder(requireContext())
        val timeStump = System.currentTimeMillis()
        Thread{
            val addressText = geocoder.getFromLocation(location.latitude,location.longitude,1)[0].getAddressLine(0)
            requireActivity().runOnUiThread {
                showAddressDialog(addressText,location)
            }
        }.start()
        Log.d("@@@"," прошло ${System.currentTimeMillis() - timeStump}")
    }


    private val locationListenerTime = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.d("@@@",location.toString())
            getAddressByLocation(location)
        }
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }

    private val locationListenerDistance = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.d("@@@",location.toString())
            getAddressByLocation(location)
        }
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                val providerGPS = locationManager.getBestProvider(criteria, true)
                /*providerGPS?.let{
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10000L,
                        0f,
                        locationListenerTime
                    )
                }*/
                providerGPS?.let{
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListenerDistance
                    )
                }
            }
        }
    }
    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


    private fun getSP() {
        isRussian = requireContext().getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
            .getBoolean(
                KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true
            )
    }

    private var isRussian = true

    private fun setFabCity() {
        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
            changeWeatherDataSetImage()
            requireContext().getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, isRussian).apply()
        }

    }



    private fun changeWeatherDataSetImage() {
        if (isRussian) {
            viewModel.getWeatherRussian()
            setImageDrawableFAB(R.drawable.ic_russia)

        } else {
            viewModel.getWeatherWorld()
            setImageDrawableFAB(R.drawable.ic_earth)
        }
    }

    private fun setImageDrawableFAB(drawable: Int) {
        binding.floatingActionButton.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawable
            )
        )
    }


    private fun initRecycler() {
        binding.recyclerView.adapter = adapter
    }

    private fun renderData(data: AppState) {

        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось", Snackbar.LENGTH_SHORT)
                    .setAction("попробовать еще раз", View.OnClickListener {
                        if (isRussian) viewModel.getWeatherRussian()
                        else viewModel.getWeatherWorld()
                    }).show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                getSP()
                adapter.setData(data.weatherListData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(Bundle().apply {
                putParcelable(
                    KEY_BUNDLE_WEATHER,
                    weather
                )
            })
        ).addToBackStack("").commit()
    }

}
