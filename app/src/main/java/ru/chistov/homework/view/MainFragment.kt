package ru.chistov.homework.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.chistov.homework.R
import ru.chistov.homework.databinding.FragmentMainBinding
import ru.chistov.homework.repository.RepositoryImpl
import ru.chistov.homework.viewmodel.AppState
import ru.chistov.homework.viewmodel.MainViewModel


class MainFragment : Fragment() {


    lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data, view)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    private fun renderData(data: AppState, view: View) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                //binding.message.text = "Не получилось ${data.error}"
                Snackbar.make(requireContext(),view,"Не получилось",Snackbar.LENGTH_SHORT).setAction("попробовать еще раз",View.OnClickListener {
                    ViewModelProvider(this).get(MainViewModel::class.java).getWeather()
                }).show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.cityName.text=data.weatherData.city.name
                binding.temperatureValue.text=data.weatherData.temperature.toString()
                binding.feelsLikeValue.text=data.weatherData.feelsLike.toString()
                binding.cityCoordinates.text="${data.weatherData.city.lat} ${data.weatherData.city.lon}"

                //binding.message.text = "Получилось"
                Snackbar.make(requireContext(),view,"работает",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MainFragment()
    }
}