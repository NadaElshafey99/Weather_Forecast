package com.example.myweatherforecastapplication.favorite.view


import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.currentLatitude
import com.example.myweatherforecastapplication.PreferenceHelper.currentLongitude
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.adapters.FavAdapter.FavAdapter
import com.example.myweatherforecastapplication.adapters.FavAdapter.SwipeItem
import com.example.myweatherforecastapplication.db.LocalSource
import com.example.myweatherforecastapplication.favorite.viewmodel.FavoriteViewModel
import com.example.myweatherforecastapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Repository
import com.example.myweatherforecastapplication.model.Welcome
import com.example.myweatherforecastapplication.network.APIClient
import com.example.myweatherforecastapplication.utils.NetworkConnection
import kotlinx.coroutines.launch
import java.util.*


class Favorite : Fragment(), OnClickListener {

    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favAdapter: FavAdapter
    private var favList: MutableList<Welcome>? = mutableListOf()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    private lateinit var addButton: Button
    private val favoriteArgs: FavoriteArgs by navArgs()
    private lateinit var hasNetworkConnection: NetworkConnection
    private lateinit var connectionGroup: Group
    private lateinit var noConnectionGroup: Group

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
        hasNetworkConnection = NetworkConnection.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        addButton = view.findViewById(R.id.add_button)
        connectionGroup = view.findViewById(R.id.connection_group)
        noConnectionGroup = view.findViewById(R.id.no_connection_group)
        favRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
                adapter = favAdapter
            }
        }
        addButton.setOnClickListener {
            when (hasNetworkConnection.isOnline()) {
                true -> {
                    Navigation.findNavController(it).navigate(R.id.navigateFromFavoriteToMaps)
                    Log.i("TAG", "fav long: ${favoriteArgs.longitude}")
                    connectionGroup.visibility = View.VISIBLE
                    noConnectionGroup.visibility = View.GONE
                }
                false -> {
                    connectionGroup.visibility = View.GONE
                    noConnectionGroup.visibility = View.VISIBLE
                }
            }
        }
        if (favoriteArgs.longitude.isNotEmpty()) {
            getWeatherOfFav(
                favoriteArgs.latitude.toDouble(),
                favoriteArgs.longitude.toDouble()
            )
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            favoriteViewModel.favorites.collect {
                favList = it as MutableList<Welcome>
                favAdapter.submitList(it)
                swipeItemToDeleteIt()
            }
        }
    }

    private fun getWeatherOfFav(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
        val state = addresses.get(0).getAdminArea() ?: addresses.toString()
        val country = addresses.get(0).getCountryName()
        val split = state.split(" ").toTypedArray()
        val timeZone = "${split[0]},$country"
        favoriteViewModel.getWeatherOfSelectedFav(latitude, longitude, requireContext())
        favoriteViewModel.weatherOfSelectedCountry.observe(viewLifecycleOwner, Observer {
            it.timezone = timeZone
            it.state = "fav"
            addFavToDB(it)
        })
    }

    override fun favDetails(welcome: Welcome) {

        when (hasNetworkConnection.isOnline()) {
            true -> {
                connectionGroup.visibility = View.VISIBLE
                noConnectionGroup.visibility = View.GONE
                val action = FavoriteDirections.navigateFromFavoriteToHomeScreen(
                    welcome.lat.toString(),
                    welcome.lon.toString(),
                    fromDestination = "fav"
                )
                Navigation.findNavController(requireView()).navigate(action)
            }
            false -> {
                connectionGroup.visibility = View.GONE
                noConnectionGroup.visibility = View.VISIBLE
            }
        }

    }

    override fun addFavToDB(welcome: Welcome) {
        favoriteViewModel.addProductToDB(welcome)
    }

    override fun removeFavFromDB(welcome: Welcome) {
        favoriteViewModel.deleteProductFromDB(welcome)
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