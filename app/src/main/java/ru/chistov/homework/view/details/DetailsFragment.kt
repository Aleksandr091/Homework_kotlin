package ru.chistov.homework.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.chistov.homework.BuildConfig
import ru.chistov.homework.databinding.FragmentDetailsBinding
import ru.chistov.homework.repository.OnErrorListener
import ru.chistov.homework.repository.OnServerResponse
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.repository.WeatherLoader
import ru.chistov.homework.repository.dto.WeatherDTO
import ru.chistov.homework.utils.*


class DetailsFragment : Fragment(), OnServerResponse, OnErrorListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireContext().unregisterReceiver(receiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            p1?.let {
                p1.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
                    ?.let { onResponse(it)
                    }
                p1.getStringExtra(KEY_MESSAGE_ERROR_SERVERSIDE)
                    ?.let { onError(it) }
                p1.getStringExtra(KEY_MESSAGE_ERROR_CLIENTSIDE)
                    ?.let { onError(it) }
                p1.let {
                    p1.getStringExtra(KEY_MESSAGE_ERROR)
                        ?.let { onError(it) }
                }

            }
        }
    }


    private lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().registerReceiver(receiver, IntentFilter(
            KEY_WAVE
        ))


        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
           /* WeatherLoader(this@DetailsFragment, this@DetailsFragment).loadWeather(
                it.city.lat,
                it.city.lon
            )*/
            requireActivity().startService(Intent(requireContext(),DetailsService::class.java).apply { putExtra(
                KEY_BUNDLE_LAT,it.city.lat)
            putExtra(KEY_BUNDLE_LON,it.city.lon)})
            //getWeather(it.city.lat,it.city.lon)
        }
    }

    /*fun getWeather(lat:Double,lon:Double){
        binding.loadingLayout.visibility = View.VISIBLE
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(API_KEY,BuildConfig.WEATHER_API_KEY)
        builder.url("${YANDEX_DOMAIN}${YANDEX_ENDPOINT}lat=$lat&lon=$lon")

        binding.loadingLayout.visibility = View.GONE

    }*/

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            with(weather) {
                cityName.text = currentCityName
                temperatureValue.text = factDTO.temperature.toString()
                feelsLikeValue.text = factDTO.feelsLike.toString()
                cityCoordinates.text = "${infoDTO.lat} ${infoDTO.lon}"
            }
        }
        //Snackbar.make(binding.mainView, "работает", Snackbar.LENGTH_SHORT).show()
        mainView.showSnackBar("работает")
    }

    private fun View.showSnackBar(it: String) {
        Snackbar.make(this, it, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }

    override fun onError(error: String) {
        mainView.showSnackBar(error)
    }


}