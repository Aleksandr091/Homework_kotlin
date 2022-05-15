package ru.chistov.homework.view.historyList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.chistov.homework.databinding.FragmentHistoryWeatherListBinding
import ru.chistov.homework.viewmodel.AppState
import ru.chistov.homework.viewmodel.HistoryViewModel


class HistoryWeatherListFragment : Fragment() {

    private var _binding: FragmentHistoryWeatherListBinding? = null
    private val binding: FragmentHistoryWeatherListBinding
        get() {
            return _binding!!
        }
    private val adapter = HistoryWeatherListAdapter()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryWeatherListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }



    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        val observer = { data: AppState -> renderData(data, view) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getAll()
    }


    private fun initRecycler() {
        binding.recyclerView.adapter = adapter
    }

    private fun renderData(data: AppState, view: View) {
        when (data) {
            is AppState.Error -> {
                /* binding.loadingLayout.visibility = View.GONE
                 Snackbar.make(binding.root, "Не получилось", Snackbar.LENGTH_SHORT)
                     .setAction("попробовать еще раз", View.OnClickListener {
                         if (isRussian) viewModel.getWeatherRussian()
                         else viewModel.getWeatherWorld()
                     }).show()*/
            }
            is AppState.Loading -> {
                //binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                // binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherListData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryWeatherListFragment()
    }


}