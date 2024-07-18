package com.example.hoangquocanh.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.hoangquocanh.MainActivity
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.local.song.loadAlbumArt
import com.example.hoangquocanh.databinding.FragmentPlayingBinding
import com.example.hoangquocanh.service.MusicService

class PlayingFragment : Fragment() {
    private var _binding: FragmentPlayingBinding? = null
    private val binding get() = _binding!!
    private var currentSong: Song? = null
    private var musicService: MusicService? = null
    private var mainActivity: MainActivity? = null
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayingBinding.inflate(inflater, container, false)
        arguments?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                currentSong = it.getParcelable("song", Song::class.java)
            } else {
                @Suppress("DEPRECATION")
                currentSong = it.getParcelable("song")
            }
        }
        progressBar = binding.progressBar
        mainActivity = requireActivity() as? MainActivity
        musicService = mainActivity!!.musicService
        mainActivity!!.removeSmallSongPlay()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            mainActivity!!.setSmallSongPlay(currentSong!!)
            parentFragmentManager.popBackStack()
        }
        binding.exitButton.setOnClickListener {
            musicService!!.stop()
            parentFragmentManager.popBackStack()
        }
        binding.songNameText.text = currentSong?.title
        binding.artistNameText.text = currentSong?.artist
        loadAlbumArt(currentSong!!.path, binding.songImage)

        musicService?.play(currentSong!!)

        musicService?.setProgressListener { progress ->
            progressBar.progress = progress
        }
        binding.playButton.setOnClickListener {
            if (musicService?.isPlaying() == true) {
                musicService?.pause()
                binding.playButton.setImageResource(R.drawable.ic_play)
            } else {
                musicService?.continuePlaying()
                binding.playButton.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

