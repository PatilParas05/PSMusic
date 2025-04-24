package com.example.psmusic

import com.google.gson.annotations.SerializedName

// Ensure this matches your project package

data class DeezerResponse(
    val data: List<SongItem>
)

data class SongItem(
    val title: String,
    val artist: Artist,
    val album: Album,
    @SerializedName("preview") val previewUrl: String // Correct API field name
)

data class Artist(
    val name: String
)

data class Album(
    val cover: String
)
