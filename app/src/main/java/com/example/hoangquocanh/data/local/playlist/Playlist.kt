package com.example.hoangquocanh.data.local.playlist

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hoangquocanh.data.local.song.Song

@Entity(tableName = "playlist")
data class Playlist (
    @PrimaryKey(autoGenerate = true)val id: Long = 0,
    @ColumnInfo(name = "image")val image: String="",
    @ColumnInfo(name = "name") val name: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(image)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}
