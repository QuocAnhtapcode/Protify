package com.example.hoangquocanh.data.local.connection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.hoangquocanh.data.local.playlist.Playlist
import com.example.hoangquocanh.data.local.song.Song

@Entity(
    tableName = "connection",
    primaryKeys = ["playlistId","songId"],
    foreignKeys = [
        ForeignKey(
            entity = Playlist::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Connection(
    @ColumnInfo(name = "songId") var songId : Long,
    @ColumnInfo(name = "playlistId") var playlistId: Long,
    @ColumnInfo(name = "order") var order: Int
)
