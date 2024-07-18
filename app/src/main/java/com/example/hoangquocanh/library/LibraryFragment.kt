package com.example.hoangquocanh.library

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.Data
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.databinding.FragmentLibraryBinding
import com.example.hoangquocanh.playlist.AddToPlaylistFragment
import com.example.hoangquocanh.song.SongItemAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val libraryViewModel: LibraryViewModel by viewModels()
    private lateinit var adapter: SongItemAdapter
    private var listRemoteSong: List<Song> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getRemoteMusicFiles()
        libraryViewModel.remoteMusicFiles.observe(viewLifecycleOwner) {
            listRemoteSong = it
        }
        val songList = binding.listSong
        adapter = SongItemAdapter(
            onClick = { song ->
                navigateToPlayingFragment(song)
            },
            onShowPopupMenu = { it, song ->
                showPopupMenu(it, song)
            },
            false
        )
        songList.adapter = adapter
        songList.layoutManager = LinearLayoutManager(context)
        updateListWithLocalMusic()
        binding.chooseLocalButton.setOnClickListener {
            updateListWithLocalMusic()
        }
        binding.chooseRemoteButton.setOnClickListener {
            updateListWithRemoteMusic()
        }
        binding.tryAgainButton.setOnClickListener {
            updateListWithRemoteMusic()
        }
    }

    private fun showPopupMenu(view: View, song: Song) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_add_to_playlist, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.add_to_playlist -> {
                    navigateToAddToPlaylistFragment(song)
                    true
                }
                R.id.share -> {
                    shareMusicFileFromUrl(song.path)
                    true
                }
                else -> false
            }
        }
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }

    private fun shareMusicFileFromUrl(url: String) {
        if (!isNetworkAvailable()) {
            showErrorAnimation()
            return
        }

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                showErrorAnimation()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    showErrorAnimation()
                    return
                }

                response.body?.let { body ->
                    val fileName = url.substring(url.lastIndexOf('/') + 1)
                    val file =
                        File(context?.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

                    val inputStream = body.byteStream()
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)
                    inputStream.close()
                    outputStream.close()

                    val uri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",
                        file
                    )

                    val shareIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uri)
                        type = "audio/*"
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    startActivity(Intent.createChooser(shareIntent, "Share Music File"))
                } ?: showErrorAnimation()
            }
        })
    }

    private fun updateListWithLocalMusic() {
        binding.listSong.visibility = View.GONE
        showLoadingAnimation(true)
        binding.chooseLocalButton.setBackgroundResource(R.drawable.bg_library_chosen_list)
        binding.chooseRemoteButton.setBackgroundResource(R.drawable.bg_library_unchosen_list)
        libraryViewModel.getLocalMusicFiles(requireContext())
        libraryViewModel.localMusicFiles.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        showLoadingAnimation(false)
        binding.listSong.visibility = View.VISIBLE
    }

    private fun updateListWithRemoteMusic() {
        binding.listSong.visibility = View.GONE
        showLoadingAnimation(true)
        binding.chooseLocalButton.setBackgroundResource(R.drawable.bg_library_unchosen_list)
        binding.chooseRemoteButton.setBackgroundResource(R.drawable.bg_library_chosen_list)
        if (!isNetworkAvailable()) {
            showErrorAnimation()
            adapter.submitList(emptyList())
        }else{
            adapter.submitList(listRemoteSong)
        }
        showLoadingAnimation(false)
        binding.listSong.visibility = View.VISIBLE
    }

    private fun showLoadingAnimation(show: Boolean) {
        binding.loadingAnimationView.visibility = if (show) View.VISIBLE else View.GONE
        binding.noConnection.visibility = View.GONE
        binding.tryAgainButton.visibility = View.GONE
    }

    private fun showErrorAnimation() {
        binding.loadingAnimationView.visibility = View.GONE
        binding.noConnection.visibility = View.VISIBLE
        binding.tryAgainButton.visibility = View.VISIBLE
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

    companion object {
        fun Fragment.navigateToPlayingFragment(song: Song) {
            val bundle = Bundle()
            bundle.putParcelable("song", song)
            findNavController().navigate(R.id.library_to_playing, bundle)
        }

        fun Fragment.navigateToAddToPlaylistFragment(song: Song) {
            val addToPlaylistFragment = AddToPlaylistFragment.newInstance(song)
            addToPlaylistFragment.show(parentFragmentManager, "AddToPlaylistFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
