package com.example.hoangquocanh.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.remote.tracks.Track
import com.example.hoangquocanh.databinding.ItemTopTracksBinding

class ItemTopTracks(private val onClick: (Track)-> Unit)
    : ListAdapter<Track, ItemTopTracks.ViewHolder>(TopTracksDiffCallBack()){

    inner class ViewHolder(private val binding: ItemTopTracksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.name.text = track.name
            if (track.artist!!.name.isNullOrEmpty()) {
                binding.artist.text = "Unknown"
            }else{
                binding.artist.text = track.artist!!.name
            }
            binding.numListen.text = track.playcount
            Glide.with(binding.root)
                .load(track.image[0].text)
                .error(R.drawable.default_small_image)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTopTracksBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    private class TopTracksDiffCallBack : DiffUtil.ItemCallback<Track>() {
        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}