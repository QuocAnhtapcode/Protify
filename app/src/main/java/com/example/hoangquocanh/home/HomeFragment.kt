package com.example.hoangquocanh.home

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.Data
import com.example.hoangquocanh.data.remote.albums.TopAlbumResponse
import com.example.hoangquocanh.data.remote.artist.TopArtistResponse
import com.example.hoangquocanh.data.remote.tracks.TopTrackResponse
import com.example.hoangquocanh.databinding.FragmentHomeBinding
import java.io.File

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeAdapter: HomeAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val itemList = mutableListOf(
        HomeItem.ImageItem(""),
        HomeItem.TitleItem("Top Albums"),
        HomeItem.TopAlbumsItem(TopAlbumResponse()),
        HomeItem.TitleItem("Top Tracks"),
        HomeItem.TopTracksItem(TopTrackResponse()),
        HomeItem.TitleItem("Top Artist"),
        HomeItem.TopArtistItem(TopArtistResponse())
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.text = Data.currentUser!!.username
        Glide.with(this)
            .load(File(Data.currentUser!!.avatar))
            .error(R.drawable.ic_default_profile)
            .into(binding.avatar)

        binding.avatar.setOnClickListener {
            findNavController().navigate(R.id.home_to_profile)
        }
        binding.tryAgainButton.setOnClickListener {
            findNavController().navigate(R.id.reset_home_fragment)
        }
        if(isNetworkAvailable()){
            binding.mainList.visibility = View.VISIBLE
            binding.noConnection.visibility = View.GONE
            binding.tryAgainButton.visibility = View.GONE
            homeViewModel.topAlbums.observe(viewLifecycleOwner) { topAlbums ->
                itemList[2] = HomeItem.TopAlbumsItem(topAlbums)
                homeAdapter.notifyDataSetChanged()
            }
            homeViewModel.topTracks.observe(viewLifecycleOwner){ topTracks->
                itemList[4] = HomeItem.TopTracksItem(topTracks)
                homeAdapter.notifyDataSetChanged()
            }
            homeViewModel.topArtist.observe(viewLifecycleOwner){ topArtist->
                itemList[6] = HomeItem.TopArtistItem(topArtist)
                homeAdapter.notifyDataSetChanged()
            }
            homeAdapter = HomeAdapter(itemList) { item ->
                when (item.getType()) {
                    HomeItem.IMAGE_TYPE -> {}
                    HomeItem.TITLE_TYPE -> {
                        if(item == HomeItem.TitleItem("Top Albums")){
                            findNavController().navigate(R.id.home_to_top_albums)
                        }else if(item == HomeItem.TitleItem("Top Tracks")){
                            findNavController().navigate(R.id.home_to_top_tracks)
                        }else if(item == HomeItem.TitleItem("Top Artist")){
                            findNavController().navigate(R.id.home_to_top_artist)
                        }
                    }
                    HomeItem.RECYCLER_VIEW_TYPE -> {}
                    HomeItem.TOP_ALBUMS_ITEM -> {}
                    HomeItem.TOP_TRACKS_ITEM -> {}
                    HomeItem.TOP_ARTIST_ITEM -> {}
                }
            }
            binding.mainList.apply {
                adapter = homeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }else{
            binding.mainList.visibility = View.GONE
            binding.noConnection.visibility = View.VISIBLE
            binding.tryAgainButton.visibility = View.VISIBLE
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.run {
            listOf(hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)).all { it }
        } == true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
