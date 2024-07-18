package com.example.hoangquocanh

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import com.example.hoangquocanh.data.Data
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.local.user.User
import com.example.hoangquocanh.databinding.ActivityMainBinding
import com.example.hoangquocanh.service.MusicService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar
    private var user: User? = null
    private var serviceIntent: Intent? = null
    internal var musicService: MusicService? = null
    private var isMusicServiceBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isMusicServiceBound = true
            Log.d("MainActivity", "MusicService connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isMusicServiceBound = false
            Log.d("MainActivity", "MusicService disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<User>("user")
        }
        Data.currentUser = user
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentManager =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHost
        navController = fragmentManager.navController
        bottomNav = binding.navView
        bottomNav.setupWithNavController(navController)
        bindService()
        progressBar = binding.progressBar
    }

    private fun bindService() {
        serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent!!, connection, Context.BIND_AUTO_CREATE)
    }

    internal fun unbindService() {
        unbindService(connection)
    }

    fun setSmallSongPlay(song: Song) {
        binding.progressBar.visibility = View.VISIBLE
        binding.smallSongPlaying.visibility = View.VISIBLE
        binding.smallSongDestroy.visibility = View.VISIBLE
        binding.songName.text = song.title
        val durationInSeconds = song.duration / 1000
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds % 60
        binding.songDuration.text = String.format("%d:%02d", minutes, seconds)
        binding.smallSongDestroy.setOnClickListener {
            removeSmallSongPlay()
            musicService!!.stop()
            isMusicServiceBound = false
        }
        binding.playButton.setOnClickListener {
            if(musicService!!.isPlaying()){
                binding.playButton.setImageResource(R.drawable.ic_play)
                musicService!!.pause()
            }else{
                binding.playButton.setImageResource(R.drawable.ic_pause)
                musicService!!.continuePlaying()
            }
        }
        musicService?.setProgressListener { progress ->
            progressBar.progress = progress
        }
    }

    fun removeSmallSongPlay() {
        binding.progressBar.visibility = View.GONE
        binding.smallSongPlaying.visibility = View.GONE
        binding.smallSongDestroy.visibility = View.GONE
    }
}