package com.example.android.colearnassignment.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.colearnassignment.Constants.ADVANCEFILTERFRAGMENT
import com.example.android.colearnassignment.Constants.ANY
import com.example.android.colearnassignment.Constants.ANY_COLOR
import com.example.android.colearnassignment.Constants.COLOR
import com.example.android.colearnassignment.Constants.FILTERPARAM
import com.example.android.colearnassignment.Constants.ISFROMFILTERPAGE
import com.example.android.colearnassignment.Constants.ORDER_BY
import com.example.android.colearnassignment.Constants.ORIENTATION
import com.example.android.colearnassignment.Constants.QUERY
import com.example.android.colearnassignment.Constants.RELEVANCE
import com.example.android.colearnassignment.R
import com.example.android.colearnassignment.adapter.GalleryAdapter
import com.example.android.colearnassignment.databinding.FragmentSearchResultsBinding
import com.example.android.colearnassignment.model.FilterParam
import com.example.android.colearnassignment.viewmodel.MainActivityViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val SEARCH_QUERY_PARAM = "search_query"
private const val FILTER_QUERY_PARAM = FILTERPARAM
private const val IS_FROM_FILTER_PAGE = ISFROMFILTERPAGE

class SearchResultsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var searchQuery: String? = null
    private var filterParam: FilterParam? = null
    private var isFromFilterPage: Boolean = false
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private lateinit var searchResultsBinding: FragmentSearchResultsBinding
    private var menu: Menu? = null

    private lateinit var galleryAdapter: GalleryAdapter
    var mPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchQuery = it.getString(SEARCH_QUERY_PARAM)
            filterParam = it.getParcelable(FILTER_QUERY_PARAM)
            isFromFilterPage = it.getBoolean(IS_FROM_FILTER_PAGE)
        }
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchResultsBinding = FragmentSearchResultsBinding.inflate(layoutInflater)
        return searchResultsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    companion object {
        @JvmStatic
        fun newInstance(searchQuery: String) =
            SearchResultsFragment().apply {
                arguments = Bundle().apply {
                    putString(SEARCH_QUERY_PARAM, searchQuery)
                }
            }
    }

    private fun initializeRecyclerView() {
        galleryAdapter = GalleryAdapter { pic ->
            (activity as SearchActivity).navigateToDetailActivity(pic)
        }
        galleryAdapter.addLoadStateListener {
            searchResultsBinding.progressBar.isVisible = it.source.refresh is LoadState.Loading
            if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached) {
                if (galleryAdapter.itemCount == 0) {
                    searchResultsBinding.blankLayout.root.visibility = View.VISIBLE
                    searchResultsBinding.progressBar.visibility = View.INVISIBLE
                    searchResultsBinding.galleryRecyclerView.visibility = View.INVISIBLE
                    val item: MenuItem = menu?.findItem(R.id.advanced_filter) as MenuItem
                    if (item != null) item.setVisible(false)
                }
            }
        }

        searchResultsBinding.galleryRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = galleryAdapter
        }
        lifecycleScope.launch(Dispatchers.Main) {
            val params: MutableMap<String, String> = HashMap()
            params[QUERY] = searchQuery as String
            if (isFromFilterPage) {
                if (filterParam?.selectedSort != "" && filterParam?.selectedSort != RELEVANCE) {
                    params[ORDER_BY] = filterParam?.selectedSort as String
                }
                if (filterParam?.selectedOrientation != "" && filterParam?.selectedOrientation != ANY) {
                    params[ORIENTATION] = filterParam?.selectedOrientation as String
                }
                if (filterParam?.selectedColor != "" && filterParam?.selectedColor != ANY_COLOR) {
                    params[COLOR] = filterParam?.selectedColor as String
                }
                if (filterParam?.selectedTone != "") {
                    params[COLOR] = filterParam?.selectedTone as String
                }
            } else {
                resetFilterParameters()
            }
            mainActivityViewModel.searchImages(params).collectLatest {
                galleryAdapter.submitData(it)
            }
        }
    }


    private fun navigateToFilterFragment() {
        val manager: FragmentManager = requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(
            R.id.layout_container_frame,
            AdvanceFilterFragment.newInstance(searchQuery),
            ADVANCEFILTERFRAGMENT
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun resetFilterParameters() {
        val filterParam =
            FilterParam("", "", "", "")
        mPrefs = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs!!.edit()
        val gson = Gson()
        val json = gson.toJson(filterParam)
        prefsEditor.putString(FILTERPARAM, json)
        prefsEditor.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
        this.menu = menu;
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.advanced_filter -> navigateToFilterFragment()
            android.R.id.home -> requireActivity().onBackPressed()
        }
        return true
    }
}