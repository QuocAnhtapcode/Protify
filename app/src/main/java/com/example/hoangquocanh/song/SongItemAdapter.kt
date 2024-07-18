package com.example.hoangquocanh.song
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hoangquocanh.data.local.song.Song
import com.example.hoangquocanh.data.local.song.loadAlbumArt
import com.example.hoangquocanh.databinding.ItemSongBinding
import com.example.hoangquocanh.databinding.ItemSongGridBinding

class SongItemAdapter(
    private val onClick: (Song) -> Unit,
    private val onShowPopupMenu: (View, Song) -> Unit,
    private val isGridView: Boolean
) : ListAdapter<Song, RecyclerView.ViewHolder>(MusicDiffCallBack()) {

    companion object {
        private const val LIST_VIEW_TYPE = 0
        private const val GRID_VIEW_TYPE = 1
    }

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.songNameText.text = song.title
            binding.artistNameText.text = song.artist
            val durationInSeconds = song.duration / 1000
            val minutes = durationInSeconds / 60
            val seconds = durationInSeconds % 60
            binding.durationText.text = String.format("%d:%02d", minutes, seconds)
            loadAlbumArt(song.path, binding.image)
            binding.image.setOnClickListener {
                onClick.invoke(song)
            }
            binding.optionButton.setOnClickListener { view ->
                onShowPopupMenu.invoke(view, song)
            }
        }
    }

    inner class ViewHolderGrid(private val binding: ItemSongGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.songNameText.text = song.title
            binding.artistNameText.text = song.artist
            val durationInSeconds = song.duration
            val minutes = durationInSeconds / 60
            val seconds = durationInSeconds % 60
            binding.durationText.text = String.format("%d:%02d", minutes, seconds)
            loadAlbumArt(song.path, binding.image)
            binding.image.setOnClickListener {
                onClick.invoke(song)
            }
            binding.optionButton.setOnClickListener { view ->
                onShowPopupMenu.invoke(view, song)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) GRID_VIEW_TYPE else LIST_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            GRID_VIEW_TYPE -> {
                val binding = ItemSongGridBinding.inflate(inflater, parent, false)
                ViewHolderGrid(binding)
            }
            else -> {
                val binding = ItemSongBinding.inflate(inflater, parent, false)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val song = getItem(position)
        when (holder) {
            is ViewHolder -> holder.bind(song)
            is ViewHolderGrid -> holder.bind(song)
        }
    }

    fun removeItem(position: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(position)
        submitList(currentList)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        val currentList = currentList.toMutableList()
        val fromItem = currentList.removeAt(fromPosition)
        currentList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, fromItem)
        submitList(currentList)
    }

    private class MusicDiffCallBack : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
}
