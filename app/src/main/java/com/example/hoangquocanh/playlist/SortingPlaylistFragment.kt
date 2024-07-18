package com.example.hoangquocanh.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.Data
import com.example.hoangquocanh.data.local.connection.ConnectionViewModelFactory
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.playlist.PlaylistViewModelFactory
import com.example.hoangquocanh.databinding.FragmentSortingPlaylistBinding
import com.example.hoangquocanh.song.SongItemAdapter

class SortingPlaylistFragment : Fragment() {
    private var _binding: FragmentSortingPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var connectionViewmodel: ConnectionViewmodel
    private lateinit var playlistViewmodel: PlaylistViewmodel
    private var playlist: Playlist? = null
    private lateinit var adapter: SongItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSortingPlaylistBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        playlistViewmodel.getSongByPlaylistId(playlist!!.id).observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.confirmButton.setOnClickListener {
            savePlaylistChanges()
            //findNavController().navigate(R.id.sorting_to_detail_playlist_fragment)
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        val songList = binding.listSong
        adapter = SongItemAdapter(
            onClick = { /* Handle item click */ },
            onShowPopupMenu = { _, _ -> /* Handle popup menu */ },
            Data.isGridView
        )
        songList.adapter = adapter
        if (Data.isGridView) {
            songList.layoutManager = GridLayoutManager(context, 2)
        } else {
            songList.layoutManager = LinearLayoutManager(context)
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(songList)
    }

    private fun savePlaylistChanges() {
        val updatedSongs = adapter.currentList
        connectionViewmodel.clearConnectionsByPlaylistId(playlist!!.id)
        var index = 0
        updatedSongs.forEach { song ->
            connectionViewmodel.updateOrderConnection(song.id, playlist!!.id, index)
            index += 1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
