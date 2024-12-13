package com.example.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.data.database.StoryEntity
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = intent.getParcelableExtra<StoryEntity>(KEY_STORY)

        storyItem?.let {
            binding.textView2.text = it.name
            binding.tvDescription.text = it.description
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.imageView2)
        }


    }

    companion object {
         const val KEY_STORY = "key_story"
    }
}