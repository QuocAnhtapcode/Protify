package com.example.hoangquocanh.home

import com.example.hoangquocanh.data.remote.albums.TopAlbumResponse
import com.example.hoangquocanh.data.remote.artist.TopArtistResponse
import com.example.hoangquocanh.data.remote.tracks.TopTrackResponse

sealed class HomeItem {
    abstract fun getType(): Int

    data class ImageItem(val imageUrl: String) : HomeItem() {
        override fun getType() = IMAGE_TYPE
    }

    data class TitleItem(val text: String) : HomeItem() {
        override fun getType() = TITLE_TYPE
    }

    data class RecyclerViewItem(val items: List<String>) : HomeItem() {
        override fun getType() = RECYCLER_VIEW_TYPE
    }
    data class TopAlbumsItem(val topAlbumResponse: TopAlbumResponse): HomeItem(){
        override fun getType() = TOP_ALBUMS_ITEM
    }
    data class TopTracksItem(val topTrackResponse: TopTrackResponse): HomeItem(){
        override fun getType() = TOP_TRACKS_ITEM
    }
    data class TopArtistItem(val topArtistResponse: TopArtistResponse): HomeItem(){
        override fun getType() = TOP_ARTIST_ITEM
    }

    companion object {
        const val IMAGE_TYPE = 0
        const val TITLE_TYPE = 1
        const val RECYCLER_VIEW_TYPE = 2
        const val TOP_ALBUMS_ITEM = 3
        const val TOP_TRACKS_ITEM = 4
        const val TOP_ARTIST_ITEM = 5
    }
}
