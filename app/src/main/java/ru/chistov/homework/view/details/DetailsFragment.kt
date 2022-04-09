package ru.chistov.homework.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import ru.chistov.homework.R
import ru.chistov.homework.databinding.FragmentDetailsBinding
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.utils.KEY_BUNDLE_WEATHER
import ru.chistov.homework.view.MainActivity
import ru.chistov.homework.view.weatherList.OnItemClickListener


class DetailsFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let { renderData(it) }
    }

    private fun renderData(weather: Weather) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            with(weather) {
                cityName.text = city.name
                temperatureValue.text = temperature.toString()
                feelsLikeValue.text = feelsLike.toString()
                cityCoordinates.text = "${city.lat} ${city.lon}"
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

}