package com.example.myweatherforecastapplication.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.adapters.FavAdapter.FavAdapter
import com.example.myweatherforecastapplication.db.LocalSource
import com.example.myweatherforecastapplication.favorite.viewmodel.FavoriteViewModel
import com.example.myweatherforecastapplication.favorite.viewmodel.FavoriteViewModelFactory
import com.example.myweatherforecastapplication.model.Favorite
import com.example.myweatherforecastapplication.model.Repository
import com.example.myweatherforecastapplication.model.Welcome
import com.example.myweatherforecastapplication.network.APIClient
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.currentLatitude
import com.example.myweatherforecastapplication.splashScreen.viewmodel.PreferenceHelper.currentLongitude
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Favorite : Fragment(), OnClickListener {

    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favAdapter: FavAdapter
    private var favList: MutableList<Favorite>? = mutableListOf()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    private lateinit var addButton: Button

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
            val action=FavoriteDirections.navigateFromFavoriteToMaps("fav")
            Navigation.findNavController(view).navigate(action)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
//        addButton.setOnClickListener {
//            val fav=Favorite(33.44, -94.04, 294.13, "10d")
//            addFavToDB(fav)
//        }
        favoriteViewModel.favorites.observe(this, Observer {
            favList=it as MutableList<Favorite>
            favAdapter.submitList(favList)
            favRecyclerView.apply {
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                    adapter = favAdapter
                }
            }
        })

    }

    override fun favDetails(favorite: Favorite) {
        favoriteViewModel.weatherOfSelectedCountry.observe(this, {
            //navigate to home screen and send args
//            favList?.add(Favorite(33.44, -94.04, 294.13, "10d"))
        })
    }

    override fun addFavToDB(favorite: Favorite) {
        favoriteViewModel.addProductToDB(favorite)
    }

    override fun removeFavFromDB(favorite: Favorite) {
        favoriteViewModel.deleteProductFromDB(favorite)
    }

}