package com.example.hoangquocanh.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hoangquocanh.R
import com.example.hoangquocanh.databinding.ItemHomeImageBinding
import com.example.hoangquocanh.databinding.ItemHomeRecyclerViewBinding
import com.example.hoangquocanh.databinding.ItemHomeTitleBinding
import com.example.hoangquocanh.home.item.ItemTopAlbumsAtHome
import com.example.hoangquocanh.home.item.ItemTopArtist
import com.example.hoangquocanh.home.item.ItemTopTracks

class HomeAdapter(
    private val listItem: List<HomeItem>,
    private val onClick: (HomeItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HomeItem.IMAGE_TYPE -> {
                val binding = ItemHomeImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }
            HomeItem.TITLE_TYPE -> {
                val binding = ItemHomeTitleBinding.inflate(inflater, parent, false)
                TitleViewHolder(binding)
            }
            HomeItem.RECYCLER_VIEW_TYPE -> {
                val binding = ItemHomeRecyclerViewBinding.inflate(inflater, parent, false)
                RecyclerViewHolder(binding)
            }
            HomeItem.TOP_ALBUMS_ITEM -> {
                val binding = ItemHomeRecyclerViewBinding.inflate(inflater, parent, false)
                HomeAlbumsViewHolder(binding)
            }
            HomeItem.TOP_TRACKS_ITEM -> {
                val binding = ItemHomeRecyclerViewBinding.inflate(inflater, parent, false)
                HomeTracksViewHolder(binding)
            }
            HomeItem.TOP_ARTIST_ITEM -> {
                val binding = ItemHomeRecyclerViewBinding.inflate(inflater, parent, false)
                HomeArtistViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listItem[position]
        when (holder.itemViewType) {
            HomeItem.IMAGE_TYPE -> (holder as ImageViewHolder).bind(item as HomeItem.ImageItem)
            HomeItem.TITLE_TYPE -> (holder as TitleViewHolder).bind(item as HomeItem.TitleItem)
            HomeItem.RECYCLER_VIEW_TYPE -> (holder as RecyclerViewHolder).bind(item as HomeItem.RecyclerViewItem)
            HomeItem.TOP_ALBUMS_ITEM -> (holder as HomeAlbumsViewHolder).bind(item as HomeItem.TopAlbumsItem)
            HomeItem.TOP_TRACKS_ITEM -> (holder as HomeTracksViewHolder).bind(item as HomeItem.TopTracksItem)
            HomeItem.TOP_ARTIST_ITEM -> (holder as HomeArtistViewHolder).bind(item as HomeItem.TopArtistItem)
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listItem[position].getType()
    }

    inner class ImageViewHolder(private val binding: ItemHomeImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageItem: HomeItem.ImageItem) {
            Glide.with(binding.root)
                .load(imageItem.imageUrl)
                .error(R.drawable.ic_rankings)
                .into(binding.image)
        }
    }

    inner class TitleViewHolder(private val binding: ItemHomeTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(titleItem: HomeItem.TitleItem) {
            binding.title.text = titleItem.text
            binding.seeAll.setOnClickListener {
                onClick.invoke(titleItem)
            }
        }
    }

    inner class RecyclerViewHolder(private val binding: ItemHomeRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recyclerViewItem: HomeItem.RecyclerViewItem) {
        }
    }

    inner class HomeAlbumsViewHolder(private val binding: ItemHomeRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topAlbumsItem: HomeItem.TopAlbumsItem) {
            val adapter = ItemTopAlbumsAtHome(onClick = {})
            val listAlbum = binding.list
            listAlbum.adapter = adapter
            listAlbum.layoutManager = GridLayoutManager(
                itemView.context, 3, GridLayoutManager.HORIZONTAL, false
            )
            adapter.submitList(topAlbumsItem.topAlbumResponse.topalbums!!.album.take(6))
        }
    }

    inner class HomeTracksViewHolder(private val binding: ItemHomeRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topTracksItem: HomeItem.TopTracksItem) {
            val adapter = ItemTopTracks(onClick = {})
            val listTracks = binding.list
            listTracks.adapter = adapter
            listTracks.layoutManager = LinearLayoutManager(
                itemView.context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter.submitList(topTracksItem.topTrackResponse.toptracks!!.track.take(5))
        }
    }

    inner class HomeArtistViewHolder(private val binding: ItemHomeRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(topArtistItem: HomeItem.TopArtistItem) {
            val adapter = ItemTopArtist(onClick = {})
            val listArtist = binding.list
            listArtist.adapter = adapter
            listArtist.layoutManager = LinearLayoutManager(
                itemView.context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter.submitList(topArtistItem.topArtistResponse.artists!!.artist.take(5))
        }
    }
}
