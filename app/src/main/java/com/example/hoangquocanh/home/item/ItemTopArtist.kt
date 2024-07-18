package com.example.hoangquocanh.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.remote.artist.Artist
import com.example.hoangquocanh.databinding.ItemTopArtistBinding

class ItemTopArtist(private val onClick: (Artist)-> Unit)
    : ListAdapter<Artist, ItemTopArtist.ViewHolder>(TopArtistDiffCallBack()){

    inner class ViewHolder(private val binding: ItemTopArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.name.text = artist.name
            Glide.with(binding.root)
                .load(artist.image[0].text)
                .error(R.drawable.default_small_image)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTopArtistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    private class TopArtistDiffCallBack : DiffUtil.ItemCallback<Artist>() {
        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }
}