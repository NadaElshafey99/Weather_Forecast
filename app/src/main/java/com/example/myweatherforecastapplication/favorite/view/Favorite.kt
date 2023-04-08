package com.example.myweatherforecastapplication.favorite.view


import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.adapters.FavAdapter.FavAdapter
import com.example.myweatherforecastapplication.adapters.FavAdapter.SwipeItem
import com.example.myweatherforecastapplication.db.LocalSource
import com.example.myweatherforecastapplication.favorite.viewmodel.FavoriteViewModel
import com.example.myweatherforecastapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Repository
import com.example.myweatherforecastapplication.network.APIClient
import java.util.*


class Favorite : Fragment(), OnClickListener {

    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favAdapter: FavAdapter
    private var favList: MutableList<Favorite>? = mutableListOf()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    private lateinit var addButton: Button
    private lateinit var prefs: SharedPreferences
    private val favoriteArgs: FavoriteArgs by navArgs()
    private lateinit var geocoder: Geocoder
    private lateinit var addresses: List<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModelFactory =
            FavoriteViewModelFactory(
                Repository.getInstance(
                    APIClient.getInstance(),
                    LocalSource.getInstance(requireContext())
                )
            )
        favoriteViewModel =
            ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteViewModel::class.java)
        favAdapter = FavAdapter(requireContext(), this)
        prefs = PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
//        prefs.itemDisplay="fav"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        addButton = view.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateFromFavoriteToMaps)
        }
        if (favoriteArgs.latitude.isNotEmpty()) {
            getWeatherOfFav(
                favoriteArgs.latitude.toDouble(),
                favoriteArgs.longitude.toDouble()
            )
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.favorites.observe(this, Observer {
            favList = it as MutableList<Favorite>
            favAdapter.submitList(favList)
            favRecyclerView.apply {
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                    adapter = favAdapter
                }
            }
            swipeItemToDeleteIt()
        })

    }

    private fun getWeatherOfFav(latitude: Double, longitude: Double) {
        favoriteViewModel.getWeatherOfSelectedFav(latitude, longitude)
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        addresses = geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
        val state = addresses.get(0).getAdminArea()
        val country = addresses.get(0).getCountryName()
        val split = state.split(" ").toTypedArray()
        val timeZone = "${split[0]},$country"
        favoriteViewModel.weatherOfSelectedCountry.observe(viewLifecycleOwner, Observer {
            val add = Favorite(
                favoriteArgs.latitude.toDouble(),
                favoriteArgs.longitude.toDouble(),
                timeZone,
                it.current.temp,
                it.current.weather.get(0).icon
            )
            addFavToDB(add)
        })
    }

    override fun favDetails(favorite: Favorite) {

//        prefs.itemDisplay="fav"
        val action = FavoriteDirections.navigateFromFavoriteToHomeScreen(
            favorite.lat.toString(),
            favorite.lon.toString(),
            "fav"
        )
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun addFavToDB(favorite: Favorite) {
        favoriteViewModel.addProductToDB(favorite)
    }

    override fun removeFavFromDB(favorite: Favorite) {
        favoriteViewModel.deleteProductFromDB(favorite)
    }

    private fun swipeItemToDeleteIt() {
        val swipeItem = object : SwipeItem(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = favList?.get(viewHolder.adapterPosition)
                val position = viewHolder.adapterPosition
                val builder = AlertDialog.Builder(requireContext())
                favList?.removeAt(viewHolder.adapterPosition)
                favAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                builder.setMessage(R.string.sureToDeleteIt)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes) { dialog, id ->
                        if (deletedItem != null)
                            removeFavFromDB(deletedItem)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.no) { dialog, id ->
                        favList?.add(position, deletedItem!!)
                        favAdapter.notifyItemInserted(position)
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeItem)
        itemTouchHelper.attachToRecyclerView(favRecyclerView)
    }


}