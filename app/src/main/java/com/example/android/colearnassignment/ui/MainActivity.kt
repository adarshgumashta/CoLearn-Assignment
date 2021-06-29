package com.example.android.colearnassignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.colearnassignment.Constants.COLLECTION_ID
import com.example.android.colearnassignment.Constants.SEARCH_QUERY
import com.example.android.colearnassignment.R
import com.example.android.colearnassignment.adapter.GalleryAdapter
import com.example.android.colearnassignment.databinding.ActivityMainBinding
import com.example.android.colearnassignment.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initializeRecyclerView()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        activityMainBinding.searchButton.setOnClickListener(this@MainActivity)
        activityMainBinding.searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                checkIfSearchTermEmpty()
                true
            } else {
                false
            }
        }
    }

    private fun checkIfSearchTermEmpty() {
        if (activityMainBinding.searchInput.text?.length == 0) {
            Toast.makeText(this, getString(R.string.search_empty_term), Toast.LENGTH_LONG)
                .show()
        } else {
            navigateToSearchActivity(activityMainBinding.searchInput.text.toString())
        }
    }

    private fun initializeRecyclerView() {
        val galleryAdapter = GalleryAdapter { pic ->
            navigateToDetailActivity(pic)
        }
        galleryAdapter.addLoadStateListener {
            activityMainBinding.progressBar.isVisible = it.source.refresh is LoadState.Loading
        }

        activityMainBinding.galleryRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = galleryAdapter
        }
        lifecycleScope.launch(Dispatchers.Main) {
            mainActivityViewModel.getImages(COLLECTION_ID).collectLatest {
                galleryAdapter.submitData(it)
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_button -> checkIfSearchTermEmpty()
        }
    }

    private fun navigateToSearchActivity(searchQuery: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.apply {
            putExtra(SEARCH_QUERY, searchQuery)
        }
        startActivity(intent)
    }


}