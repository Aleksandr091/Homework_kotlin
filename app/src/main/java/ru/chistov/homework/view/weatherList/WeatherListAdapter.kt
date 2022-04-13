package ru.chistov.homework.view.weatherList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.chistov.homework.databinding.FragmentWeatherListRecyclerItemBinding
import ru.chistov.homework.repository.Weather

class WeatherListAdapter(
    private val onItemClickListener: OnItemClickListener,
    private var data: List<Weather> = listOf()
) : RecyclerView.Adapter<WeatherListAdapter.CityHolder>() {

    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            FragmentWeatherListRecyclerItemBinding.bind(itemView).apply {
                with(weather){
                    tvCityName.text = city.name
                    root.setOnClickListener {
                        onItemClickListener.onItemClick(this)
                    }
                }

            }


        }
    }
}