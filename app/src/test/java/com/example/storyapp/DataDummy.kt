package com.example.storyapp

import com.example.storyapp.data.database.StoryEntity

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "name + $i",
                "desc $i",
                "url $i",
                "date $i",
                ("lat $i").replace("lat ", "").toDouble(),
                ("lon $i").replace("lon ", "").toDouble()
            )
            items.add(story)
        }
        return items
    }
}