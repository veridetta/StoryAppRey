package com.example.storryapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storryapp.R
import com.example.storryapp.data.model.UserModel
import com.example.storryapp.databinding.ActivityListStoryBinding
import com.example.storryapp.view.adapter.LoadingStateAdapter
import com.example.storryapp.view.adapter.StoriesAdapter
import com.example.storryapp.view.ViewModelFactory
import com.example.storryapp.view.adapter.StoryLoadStateAdapter
import com.example.storryapp.view.viewmodel.ListStoryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListStoryActivity : AppCompatActivity() {

    private var _binding: ActivityListStoryBinding? = null
    private val binding get() = _binding

    private lateinit var user: UserModel
    private lateinit var adapter: StoriesAdapter

    private val viewModel: ListStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        initAdapter()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        addStoryAction()
//        user = intent.getParcelableExtra(EXTRA_USER)!!
//        setListStory()
        adapter = StoriesAdapter()
//        showSnackBar()
        setupRecycleView()
//        showHaveDataOrNot()
       // adapter.notifyDataSetChanged()

    }

    private fun initAdapter() {
        user = intent.getParcelableExtra(EXTRA_USER)!!
        adapter = StoriesAdapter()
        binding?.rvStory?.adapter = adapter.withLoadStateHeaderAndFooter(
            footer = LoadingStateAdapter { adapter.retry() },
            header = LoadingStateAdapter { adapter.retry() }
        )
        binding?.rvStory?.layoutManager = LinearLayoutManager(this)
        binding?.rvStory?.setHasFixedSize(true)

        lifecycleScope.launchWhenCreated {
//            adapter.loadStateFlow.collect {
//                binding?.swipeRefresh?.isRefreshing = it.mediator?.refresh is LoadState.Loading
//            }
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding!!.viewError.root.isVisible = loadStates.refresh is LoadState.Error
            }
            if (adapter.itemCount < 1) binding?.viewError?.root?.visibility = View.VISIBLE
            else binding?.viewError?.root?.visibility = View.GONE
        }

        viewModel.getStory(user.token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                val moveToListStoryActivity = Intent(this@ListStoryActivity,
                    MapsActivity::class.java)
                moveToListStoryActivity.putExtra(EXTRA_USER, user)
                startActivity(moveToListStoryActivity)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupRecycleView(){
        binding?.rvStory?.layoutManager = LinearLayoutManager(this)
        binding?.rvStory?.setHasFixedSize(true)
        binding?.rvStory?.adapter = adapter.withLoadStateFooter(
            footer = StoryLoadStateAdapter {
                adapter.retry()
            }
        )
        binding?.btnRetry?.setOnClickListener {
            adapter.retry()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun addStoryAction(){
        binding?.ivAddStory?.setOnClickListener {
            val moveToAddStoryActivity = Intent(this, AddStoryActivity::class.java)
            moveToAddStoryActivity.putExtra(AddStoryActivity.EXTRA_USER, user)
            startActivity(moveToAddStoryActivity)
        }
    }

    companion object {
        const val EXTRA_USER = "user"
//        const val ARRAY_LIST_STORIES = "array_list_story"
    }
}