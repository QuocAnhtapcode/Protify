package com.example.hoangquocanh.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.playlist.PlaylistViewModelFactory
import com.example.hoangquocanh.databinding.FragmentAddPlaylistBinding

class AddPlaylistFragment : DialogFragment() {
    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var playlistViewmodel: PlaylistViewmodel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        val factory = PlaylistViewModelFactory(requireActivity().application)
        playlistViewmodel = ViewModelProvider(requireActivity(), factory)[PlaylistViewmodel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.createButton.setOnClickListener {
            val playlistName = binding.nameText.text.toString()
            if (playlistName.isNotBlank()) {
                playlistViewmodel.addPlaylist(Playlist(name = playlistName))
                dismiss()
            } else {
                binding.nameText.error = "Name cannot be empty"
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance(): AddPlaylistFragment {
            val fragment = AddPlaylistFragment()
            return fragment
        }
    }
}