package com.example.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.database.StoryEntity
import com.example.storyapp.databinding.ItemStoriesBinding
import com.example.storyapp.ui.detail.DetailActivity
import com.example.storyapp.utils.formatDate

class HomeAdapter : PagingDataAdapter<StoryEntity, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder (private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: StoryEntity){
            val formatter = formatDate(data.createdAt)
            binding.cardTitle.text = data.name
            binding.textView.text = data.description
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .fitCenter()
                .into(binding.imgItemPhoto)
            binding.tvCreatedAt.text = formatter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intentDetail = Intent(context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.KEY_STORY, item)

            if (context is Activity){
                val binding = ItemStoriesBinding.bind(holder.itemView)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context,
                        androidx.core.util.Pair(binding.imgItemPhoto, "image"),
                        androidx.core.util.Pair(binding.cardTitle, "name"),
                        androidx.core.util.Pair(binding.textView, "description")
                    )
                context.startActivity(intentDetail, optionsCompat.toBundle())
            }else {
                context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.createdAt == newItem.createdAt &&
                        oldItem.photoUrl == newItem.photoUrl
            }
        }
    }
}