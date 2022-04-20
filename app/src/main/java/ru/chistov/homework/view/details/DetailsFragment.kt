package ru.chistov.homework.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import ru.chistov.homework.databinding.FragmentDetailsBinding
import ru.chistov.homework.repository.*
import ru.chistov.homework.utils.KEY_BUNDLE_WEATHER
import ru.chistov.homework.viewmodel.AppErrorState


class DetailsFragment : Fragment(),OnServerResponse,OnErrorListener {

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
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    lateinit var currentCityName:String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let{
            currentCityName =it.city.name
            WeatherLoader(this@DetailsFragment,this@DetailsFragment).loadWeather(it.city.lat,it.city.lon)

        }
    }

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