package ru.chistov.homework.view.weatherList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.chistov.homework.R
import ru.chistov.homework.databinding.FragmentWeatherListBinding
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.utils.KEY_BUNDLE_WEATHER
import ru.chistov.homework.view.details.DetailsFragment
import ru.chistov.homework.viewmodel.AppState
import ru.chistov.homework.viewmodel.MainViewModel


class WeatherListFragment : Fragment(),OnItemClickListener {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    var isRussian: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data, view)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherRussian()
                binding.floatingActionButton.setImageDrawable(ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_russia))
            } else {
                viewModel.getWeatherWorld()
                binding.floatingActionButton.setImageDrawable(ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_earth))
            }
        }
        viewModel.getWeatherRussian()
    }

    private fun renderData(data: AppState, view: View) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                /*binding.message.text = "Не получилось ${data.error}"
                Snackbar.make(requireContext(),view,"Не получилось",Snackbar.LENGTH_SHORT).setAction("попробовать еще раз",View.OnClickListener {
                    ViewModelProvider(this).get(MainViewModel::class.java).getWeather()
                }).show()*/
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherListData)
                /*binding.cityName.text=data.weatherData.city.name
                binding.temperatureValue.text=data.weatherData.temperature.toString()
                binding.feelsLikeValue.text=data.weatherData.feelsLike.toString()
                binding.cityCoordinates.text="${data.weatherData.city.lat} ${data.weatherData.city.lon}"

                //binding.message.text = "Получилось"
                Snackbar.make(requireContext(),view,"работает",Snackbar.LENGTH_SHORT).show()*/
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                WeatherListFragment()
    }
    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_WEATHER,weather)

            requireActivity().supportFragmentManager.beginTransaction().add(R.id.container,DetailsFragment.newInstance(bundle)).addToBackStack("").commit()


    }
}