package com.example.storryapp.view.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storryapp.R
import com.example.storryapp.data.network.response.ListStoryItem
import com.example.storryapp.databinding.ActivityDetailStoryBinding
import com.example.storryapp.view.viewmodel.DetailStoryViewModel
import com.example.storryapp.view.withDateFormat

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var detailStories: ListStoryItem
    private lateinit var binding: ActivityDetailStoryBinding
    private val vm: DetailStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.title = "Detail Story"
        actionBar?.setDisplayHomeAsUpEnabled(true)

//        detailStories = intent.getParcelableExtra<ListStoryItem>(STORIES) as ListStoryItem
        detailStories = intent.getParcelableExtra(EXTRA_STORY)!!
        vm.setDetailStory(detailStories)

        binding.apply {
            tvName.text = vm.storyItem.name
            tvDescription.text = vm.storyItem.description
            tvCreatedTime.text = vm.storyItem.createdAt.withDateFormat()
        }
        Glide.with(applicationContext)
            .load(detailStories.photoUrl)
            .into(binding.ivStory)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> {
                Intent(this, Setting::class.java).also {
                    startActivity(it)
                }
            }
            R.id.map -> {
                Intent(this, MapsActivity::class.java).also {
                    startActivity(it)
                }
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_STORY = "detail_story"
        const val STORIES = "stories"
    }
}


