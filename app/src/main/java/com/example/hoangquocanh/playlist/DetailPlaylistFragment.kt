package com.example.hoangquocanh.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.Data
import com.example.hoangquocanh.data.local.connection.ConnectionViewModelFactory
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.playlist.PlaylistViewModelFactory
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.databinding.FragmentDetailPlaylistBinding
import com.example.hoangquocanh.song.SongItemAdapter

class DetailPlaylistFragment : Fragment() {
    private var _binding: FragmentDetailPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var connectionViewmodel: ConnectionViewmodel
    private lateinit var playlistViewmodel: PlaylistViewmodel
    private var playlist: Playlist? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailPlaylistBinding.inflate(inflater, container, false)
        arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                playlist = it.getParcelable("playlist", Playlist::class.java)
            } else {
                @Suppress("DEPRECATION")
                playlist = it.getParcelable("playlist")
            }
        }
        val factory1 = ConnectionViewModelFactory(requireActivity().application)
        connectionViewmodel =
            ViewModelProvider(requireActivity(),factory1)[ConnectionViewmodel::class.java]
        val factory2 = PlaylistViewModelFactory(requireActivity().application)
        playlistViewmodel =
            ViewModelProvider(requireActivity(),factory2)[PlaylistViewmodel::class.java]
        if (Data.isGridView) {
            binding.changeLayout.setImageResource(R.drawable.ic_grid)
        } else {
            binding.changeLayout.setImageResource(R.drawable.ic_linear)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.detail_playlist_to_playlist)
        }
        binding.sortButton.setOnClickListener {
            navigateToSortingPlaylistFragment(playlist!!)
        }
        binding.name.text = playlist!!.name
        binding.changeLayout.setOnClickListener {
            if (Data.isGridView) {
                Data.isGridView = false
                binding.changeLayout.setImageResource(R.drawable.ic_linear)
            } else {
                Data.isGridView = true
                binding.changeLayout.setImageResource(R.drawable.ic_grid)
            }
            resetFragment(playlist!!)
        }
        val songList = binding.listSong
        val adapter = SongItemAdapter(
            onClick = { music ->
                navigateToPlayingFragment(music)
            },
            onShowPopupMenu = { view, music ->
                showPopupMenu(view, music, playlist!!)
            },
            Data.isGridView
        )
        songList.adapter = adapter
        if (Data.isGridView) {
            songList.layoutManager = GridLayoutManager(context, 2)
        } else {
            songList.layoutManager = LinearLayoutManager(context)
        }
        playlistViewmodel.getSongByPlaylistId(playlist!!.id).observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }
    private fun showPopupMenu(view: View, song: Song, playlist: Playlist) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_remove_from_playlist, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.remove_from_playlist -> {
                    connectionViewmodel.deleteConnection(song.id, playlist.id)
                    true
                }
                R.id.share -> {
                    Log.d("Share", song.path)
                    true
                }
                else -> false
            }
        }
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }

    companion object {
        fun Fragment.navigateToSortingPlaylistFragment(playlist: Playlist) {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            findNavController().navigate(R.id.detail_playlist_to_sorting_playlist, bundle)
        }

        fun Fragment.resetFragment(playlist: Playlist) {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            findNavController().navigate(R.id.reset_detail_playlist_fragment, bundle)
        }

        fun Fragment.navigateToPlayingFragment(song: Song) {
            val bundle = Bundle()
            bundle.putParcelable("song", song)
            findNavController().navigate(R.id.detail_playlist_to_playing, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}