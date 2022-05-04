package ru.chistov.homework.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import ru.chistov.homework.databinding.FragmentDetailsBinding
import ru.chistov.homework.repository.OnErrorListener
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.utils.KEY_BUNDLE_WEATHER
import ru.chistov.homework.viewmodel.DetailsState
import ru.chistov.homework.viewmodel.DetailsViewModel


class DetailsFragment : Fragment(), OnErrorListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<DetailsState> {
            override fun onChanged(t: DetailsState) {
                renderData(t)
            }

        })

        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)
            /* WeatherLoader(this@DetailsFragment, this@DetailsFragment).loadWeather(
                 it.city.lat,
                 it.city.lon
             )
             requireActivity().startService(Intent(requireContext(),DetailsService::class.java).apply { putExtra(
                 KEY_BUNDLE_LAT,it.city.lat)
             putExtra(KEY_BUNDLE_LON,it.city.lon)})
            getWeather(it.city.lat, it.city.lon)*/
        }
    }

    /*private fun getWeather(lat: Double, lon: Double) {
        binding.loadingLayout.visibility = View.VISIBLE
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("${YANDEX_DOMAIN}${YANDEX_ENDPOINT}lat=$lat&lon=$lon")
        val request = builder.build()
        val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                binding.loadingLayout.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
                    requireActivity().runOnUiThread{
                        renderData(weatherDTO)}
                }
            }
        }
        val call = client.newCall(request)
        call.enqueue(callback)
    }*/


    private fun renderData(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Error ->{
                binding.loadingLayout.visibility = View.GONE
                mainView.showSnackBar("${detailsState.error}")}
            DetailsState.Loading ->
                binding.loadingLayout.visibility = View.VISIBLE
            is DetailsState.Success -> {
                val weather = detailsState.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                    /*Glide.with(requireContext())
                        .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                        .into(headerCityIcon)

                    Picasso.get()?.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")?.into(headerCityIcon)*/

                    headerCityIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")

                    icon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")

                }
                mainView.showSnackBar("работает")
            }
        }

    }
    private fun ImageView.loadSvg(url:String){
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
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

    override fun onError(error: String) {
        mainView.showSnackBar(error)
    }


}