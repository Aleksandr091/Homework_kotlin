package ru.chistov.homework.view.weatherList

import android.content.Context
import android.os.Bundle
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
import ru.chistov.homework.repository.Weather
import ru.chistov.homework.utils.KEY_BUNDLE_WEATHER
import ru.chistov.homework.utils.KEY_SP_FILE_NAME_1
import ru.chistov.homework.utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
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

    /*private var isRussian = requireActivity()
        .getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        .getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true)*/
    private var isRussian = true


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
        val observer = Observer<AppState> { data -> renderData(data, view) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        setFloatingActionButton()
        viewModel.getWeatherRussian()

    }

    private fun setFloatingActionButton() {

        binding.floatingActionButton.setOnClickListener {

            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherRussian()
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_russia
                    )
                )
            } else {
                viewModel.getWeatherWorld()
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_earth
                    )
                )
            }
            requireContext().getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, isRussian).apply()
        }

    }

    private fun initRecycler() {
        binding.recyclerView.adapter = adapter
    }

    private fun renderData(data: AppState, view: View) {

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
