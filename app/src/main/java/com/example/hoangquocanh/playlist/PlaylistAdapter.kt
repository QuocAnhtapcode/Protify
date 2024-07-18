package com.example.hoangquocanh.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.databinding.ItemPlaylistBinding

class PlaylistAdapter(private val onClick:(Playlist)->Unit,
                      private val onLongClick:(Playlist)->Unit)
    :ListAdapter<Playlist, PlaylistAdapter.ViewHolder>(PlaylistDiffCallBack()){
    inner class ViewHolder(private val binding: ItemPlaylistBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(playlist: Playlist){
            binding.name.text = playlist.name
            binding.numSongs.text = " "
            binding.playlistHolder.setOnClickListener {
                onClick.invoke(playlist)
            }
            binding.playlistHolder.setOnLongClickListener {
                onLongClick.invoke(playlist)
                true
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaylistBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    private class PlaylistDiffCallBack: DiffUtil.ItemCallback<Playlist>(){
        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }
}