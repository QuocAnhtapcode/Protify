package com.example.hoangquocanh.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hoangquocanh.data.local.connection.ConnectionViewModelFactory
import com.example.hoangquocanh.data.local.playlist.PlaylistViewModelFactory
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.databinding.FragmentAddToPlaylistBinding


class AddToPlaylistFragment : DialogFragment() {
    private var _binding: FragmentAddToPlaylistBinding? = null
    private val binding get() = _binding!!
    private var song: Song? = null
    private lateinit var playlistViewmodel: PlaylistViewmodel
    private lateinit var connectionViewmodel: ConnectionViewmodel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddToPlaylistBinding.inflate(inflater, container, false)
        arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                song = it.getParcelable("song", Song::class.java)
            } else {
                @Suppress("DEPRECATION")
                song = it.getParcelable<Song?>("song")
            }
        }
        val factory1 = PlaylistViewModelFactory(requireActivity().application)
        playlistViewmodel =
            ViewModelProvider(requireActivity(), factory1)[PlaylistViewmodel::class.java]
        val factory2 = ConnectionViewModelFactory(requireActivity().application)
        connectionViewmodel =
            ViewModelProvider(requireActivity(), factory2)[ConnectionViewmodel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPlaylistButton.setOnClickListener {
            navigateToAddPlaylistFragment()
        }
        playlistViewmodel.playlistList.observe(viewLifecycleOwner) { it ->
            if (it.isNullOrEmpty()) {
                binding.addPlaylistButton.visibility = View.VISIBLE
                binding.listPlaylist.visibility = View.GONE
            } else {
                binding.addPlaylistButton.visibility = View.GONE
                val playlistList = binding.listPlaylist
                val adapter = PlaylistAdapter(
                    onClick = { playlist ->
                        connectionViewmodel.addConnection(song!!, playlist.id)
                        dismiss()
                    },
                    onLongClick = {})
                playlistList.adapter = adapter
                playlistList.layoutManager = LinearLayoutManager(context)
                adapter.submitList(it)
                playlistList.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(song: Song): AddToPlaylistFragment {
            val fragment = AddToPlaylistFragment()
            val bundle = Bundle().apply {
                putParcelable("song", song)
            }
            fragment.arguments = bundle
            return fragment
        }

        fun Fragment.navigateToAddPlaylistFragment() {
            val addPlaylistFragment = AddPlaylistFragment.newInstance()
            addPlaylistFragment.show(parentFragmentManager, "AddPlayListFragment")
        }
    }
}