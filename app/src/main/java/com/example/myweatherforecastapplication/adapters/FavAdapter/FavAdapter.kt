package com.example.myweatherforecastapplication.adapters.FavAdapter

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.favorite.view.OnClickListener
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Icon
import com.example.myweatherforecastapplication.model.Welcome
import java.util.*

class FavAdapter(var context: Context, var onClickListener: OnClickListener) :
    ListAdapter<Welcome, FavAdapter.FavViewHolder>(
        MyDiffUtil()
    ) {

    class FavViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherIcon: ImageView
            get() = itemView.findViewById(R.id.image_of_one_item)

        val weatherDegree: TextView
            get() = itemView.findViewById(R.id.degree_of_one_item)

        val countryName: TextView
            get() = itemView.findViewById(R.id.country_of_one_item)

        val container: ConstraintLayout
            get() = itemView.findViewById(R.id.constraint)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.design_one_item_for_favorite,
            parent,
            false
        )
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {

        val currentFavItem: Welcome = getItem(position)
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(currentFavItem.lat ?: 0.0, currentFavItem.lon ?: 0.0, 1) as List<Address>
        val state = addresses.get(0).getAdminArea()
        val country = addresses.get(0).getCountryName()
        val split = state.split(" ").toTypedArray()
        val timeZone = "${split[0]} \n $country"
        holder.weatherDegree.text = "${currentFavItem.current?.temp}"
        holder.countryName.text = timeZone
        val icon = currentFavItem.current?.weather?.get(0)?.icon?.lowercase()
        val imageResource: Int =
            context.resources.getIdentifier(
                Icon.getIcon(icon ?: "01"),
                "drawable",
                context.packageName
            )
        holder.weatherIcon.setImageResource(imageResource)
        holder.container.setOnClickListener {
            onClickListener.favDetails(currentFavItem)
        }


    }

}