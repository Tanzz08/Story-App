package com.example.storyapp.data.response

data class StoryWithLocation(
    val story: ListStoryItem,
    val lat: Double,
    val lon: Double,
)
