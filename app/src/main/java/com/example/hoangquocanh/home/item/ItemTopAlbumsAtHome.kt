package com.example.hoangquocanh.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import com.example.hoangquocanh.data.remote.albums.Album
import com.example.hoangquocanh.databinding.ItemTopAlbumsAtHomeBinding

class ItemTopAlbumsAtHome(private val onClick: (Album) -> Unit) :
    ListAdapter<Album, ItemTopAlbumsAtHome.ViewHolder>(TopAlbumsDiffCallBack()) {
    inner class ViewHolder(private val binding: ItemTopAlbumsAtHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.name.text = album.name
            if (album.artist!!.name.isNullOrEmpty()) {
                binding.artist.text = "Unknown"
            }else{
                binding.artist.text = album.artist!!.name
            }
            Glide.with(binding.root)
                .load(album.image[0].text)
                .error(R.drawable.default_small_image)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTopAlbumsAtHomeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class TopAlbumsDiffCallBack : DiffUtil.ItemCallback<Album>() {
        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}