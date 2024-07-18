package com.example.hoangquocanh.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.playlist.PlaylistViewModelFactory
import com.example.hoangquocanh.databinding.FragmentPlaylistBinding

class PlaylistFragment: Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistViewmodel: PlaylistViewmodel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater,container,false)
        val factory = PlaylistViewModelFactory(requireActivity().application)
        playlistViewmodel = ViewModelProvider(requireActivity(), factory)[PlaylistViewmodel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPlaylistButton.setOnClickListener{
            val addPlaylistFragment = AddPlaylistFragment()
            addPlaylistFragment.show(
                (activity as AppCompatActivity).supportFragmentManager,"addPlaylist"
            )
        }
        binding.addPlaylistSmallButton.setOnClickListener {
            val addPlaylistFragment = AddPlaylistFragment()
            addPlaylistFragment.show(
                (activity as AppCompatActivity).supportFragmentManager,"addPlaylist"
            )
        }
        playlistViewmodel.playlistList.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                binding.listPlaylist.visibility = View.GONE
                binding.addPlaylistSmallButton.visibility = View.GONE
                binding.addPlaylistButton.visibility = View.VISIBLE
            }else{
                binding.addPlaylistButton.visibility = View.GONE
                val playlistList = binding.listPlaylist
                val adapter = PlaylistAdapter(
                    onClick = {playlist->
                        navigateToDetailPlaylistFragment(playlist)
                    },
                    onLongClick = {playlist->
                        val alertDialogBuilder = AlertDialog.Builder(requireContext())
                        alertDialogBuilder.setTitle("You want to delete ${playlist.name}")
                        alertDialogBuilder.setPositiveButton("DELETE") { _, _ ->
                            playlistViewmodel.deletePlaylist(playlist)
                        }
                        alertDialogBuilder.setNegativeButton("BACK") { _, _ -> }
                        val alertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    })
                playlistList.adapter = adapter
                playlistList.layoutManager = LinearLayoutManager(context)
                adapter.submitList(it)
                binding.listPlaylist.visibility = View.VISIBLE
                binding.addPlaylistSmallButton.visibility = View.VISIBLE
            }
        }

    }
    companion object {
        fun Fragment.navigateToDetailPlaylistFragment(playlist: Playlist) {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            findNavController().navigate(R.id.playlist_to_detail_playlist,bundle)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}