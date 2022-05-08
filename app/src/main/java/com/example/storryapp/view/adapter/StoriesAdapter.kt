package com.example.storryapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storryapp.R
import com.example.storryapp.data.network.response.ListStoryItem
import com.example.storryapp.databinding.ItemListStoryBinding
import com.example.storryapp.view.activity.DetailStoryActivity
import com.example.storryapp.view.withDateFormat

class StoriesAdapter : PagingDataAdapter<ListStoryItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

//    private val listStory = ArrayList<ListStoryItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_story, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

//    override fun getItemCount(): Int = listStory.size

    class ViewHolder(private var binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root){
        private var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_image)
        private var tvUsername: TextView = itemView.findViewById(R.id.tv_name)
        private var tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private var tvCreated: TextView = itemView.findViewById(R.id.tv_created_time)

        fun bind(story: ListStoryItem){
            tvUsername.text = story.name
            tvDescription.text = story.description
            tvCreated.text = story.createdAt.withDateFormat()

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_error_24)
                .into(imgPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(STORIES, story)

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(imgPhoto, "profile"),
                    Pair(tvUsername, "name"),
                    Pair(tvDescription, "description"),
                    Pair(tvCreated, "createdAt"),
                )
                val itemView = Intent(it.context, DetailStoryActivity::class.java)
                itemView.putExtra(DetailStoryActivity.EXTRA_STORY, story)
                it.context.startActivity(itemView, optionsCompat.toBundle())
//                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        const val STORIES = "stories"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}