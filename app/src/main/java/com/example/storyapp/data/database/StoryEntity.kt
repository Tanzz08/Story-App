package com.example.storyapp.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "photoUrl")
    var photoUrl: String,

    @ColumnInfo(name = "createdAt")
    var createdAt: String,

    @ColumnInfo(name = "lat")
    var lat: Double,

    @ColumnInfo(name = "lon")
    var lon: Double
): Parcelable