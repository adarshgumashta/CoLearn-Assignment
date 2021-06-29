package com.example.android.colearnassignment.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android.colearnassignment.Constants.SEARCHRESULTSFRAGMENT
import com.example.android.colearnassignment.Constants.SEARCH_QUERY
import com.example.android.colearnassignment.R
import com.example.android.colearnassignment.databinding.ActivitySearchBinding


class SearchActivity : BaseActivity() {
    private lateinit var searchActivityBinding: ActivitySearchBinding
    private lateinit var searchQuery: String
    private lateinit var searchResultsFragment: SearchResultsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchActivityBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchActivityBinding.root)
        searchQuery = intent.getStringExtra(SEARCH_QUERY).toString()
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        searchResultsFragment = SearchResultsFragment.newInstance(searchQuery)
        transaction.add(
            R.id.layout_container_frame,
            searchResultsFragment,
            SEARCHRESULTSFRAGMENT
        )
        transaction.addToBackStack(null)
        transaction.commit()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = searchQuery
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            else -> if (searchResultsFragment != null) searchResultsFragment.onOptionsItemSelected(
                item
            )
        }
        return true
    }

}