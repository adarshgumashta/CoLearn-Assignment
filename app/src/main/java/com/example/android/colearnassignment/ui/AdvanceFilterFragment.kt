package com.example.android.colearnassignment.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.android.colearnassignment.Constants.ANY
import com.example.android.colearnassignment.Constants.BLACK_AND_WHITE
import com.example.android.colearnassignment.Constants.BLUE
import com.example.android.colearnassignment.Constants.FILTERPARAM
import com.example.android.colearnassignment.Constants.GREEN
import com.example.android.colearnassignment.Constants.ISFROMFILTERPAGE
import com.example.android.colearnassignment.Constants.LANDSCAPE
import com.example.android.colearnassignment.Constants.LATEST
import com.example.android.colearnassignment.Constants.ORANGE
import com.example.android.colearnassignment.Constants.PORTRAIT
import com.example.android.colearnassignment.Constants.PURPLE
import com.example.android.colearnassignment.Constants.RED
import com.example.android.colearnassignment.Constants.SEARCHRESULTSFRAGMENT
import com.example.android.colearnassignment.Constants.SEARCH_QUERY
import com.example.android.colearnassignment.Constants.SQUARE
import com.example.android.colearnassignment.Constants.YELLOW
import com.example.android.colearnassignment.R
import com.example.android.colearnassignment.databinding.FragmentAdvanceFilterBinding
import com.example.android.colearnassignment.model.FilterParam
import com.google.gson.Gson

private const val SEARCH_QUERY_PARAM = "search_query"

class AdvanceFilterFragment : Fragment(), View.OnClickListener {
    private var isChecking: Boolean = true
    private var mCheckedId: Int = 0
    private lateinit var binding: FragmentAdvanceFilterBinding
    var mPrefs: SharedPreferences? = null
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchQuery = it.getString(SEARCH_QUERY_PARAM)

        }
        mPrefs = activity?.getPreferences(MODE_PRIVATE)
        setHasOptionsMenu(true);

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().actionBar?.title = getString(R.string.filter_menu)
        binding = FragmentAdvanceFilterBinding.inflate(layoutInflater)
        binding.searchFilter.setOnClickListener(this)
        binding.clearFilter.setOnClickListener(this)
        binding.cancelButton.setOnClickListener(this)
        binding.colorByRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                binding.tonesByRadioGroup.clearCheck()
                mCheckedId = checkedId
            }
            isChecking = true
        })

        binding.tonesByRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                binding.colorByRadioGroup.clearCheck()
                mCheckedId = checkedId
            }
            isChecking = true
        })
        setPreviouslySelectedValues()
        return binding.root
    }

    private fun setPreviouslySelectedValues() {
        val gson = Gson()
        val json = mPrefs!!.getString(FILTERPARAM, "")
        if (json != null && json != "") {
            val obj: FilterParam = gson.fromJson(json, FilterParam::class.java)
            when (obj.selectedColor) {
                BLACK_AND_WHITE -> binding.blackAndWhite.isChecked = true
                ANY -> binding.anyColor.isChecked = true
            }
            when (obj.selectedOrientation) {
                PORTRAIT -> binding.portrait.isChecked = true
                SQUARE -> binding.squarish.isChecked = true
                LANDSCAPE -> binding.landscape.isChecked = true
            }
            when (obj.selectedSort) {
                LATEST -> binding.latest.isChecked = true
            }
            when (obj.selectedTone) {
                RED -> binding.red.isChecked = true
                ORANGE -> binding.orange.isChecked = true
                GREEN -> binding.green.isChecked = true
                PURPLE -> binding.purple.isChecked = true
                YELLOW -> binding.yellow.isChecked = true
                BLUE -> binding.blue.isChecked = true
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(searchQuery: String?) =
            AdvanceFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(SEARCH_QUERY_PARAM, searchQuery)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_filter -> applyFilters()
            R.id.clear_filter -> clearFilters()
            R.id.cancel_button -> fragmentManager?.popBackStack();
        }
    }

    private fun goBackToSearchPage(filterParam: FilterParam) {
        val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        val frag = SearchResultsFragment()
        val bundles = Bundle()
        bundles.putParcelable(FILTERPARAM, filterParam);
        bundles.putBoolean(ISFROMFILTERPAGE, true);
        bundles.putString(SEARCH_QUERY, searchQuery);
        frag.arguments = bundles
        ft.replace(R.id.layout_container_frame, frag, SEARCHRESULTSFRAGMENT)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun clearFilters() {
        val filterParam =
            FilterParam("", "", "", "")
        callAPIAfterFilter(filterParam)
    }

    fun callAPIAfterFilter(filterParam: FilterParam) {
        val prefsEditor: SharedPreferences.Editor = mPrefs!!.edit()
        val gson = Gson()
        val json = gson.toJson(filterParam)
        prefsEditor.putString(FILTERPARAM, json)
        prefsEditor.commit()
        goBackToSearchPage(filterParam)
    }

    private fun applyFilters() {
        val selectedSortRB = binding.sortByRadioGroup.checkedRadioButtonId
        val selectedColorRB = binding.colorByRadioGroup.checkedRadioButtonId
        val selectedOrientationRB = binding.orientationRadioGroup.checkedRadioButtonId
        val selectedtonesRB = binding.tonesByRadioGroup.checkedRadioButtonId
        var selectedColor: String = ""
        var selectedTone: String = ""
        val selectedSort = resources.getResourceEntryName(selectedSortRB)
        if (selectedColorRB != -1) {
            selectedColor = resources.getResourceEntryName(selectedColorRB)
        }
        if (selectedtonesRB != -1) {
            selectedTone = resources.getResourceEntryName(selectedtonesRB)
        }
        val selectedOrientation = resources.getResourceEntryName(selectedOrientationRB)
        val filterParam =
            FilterParam(selectedColor, selectedTone, selectedOrientation, selectedSort)
        callAPIAfterFilter(filterParam)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.advanced_filter)
        if (item != null) item.setVisible(false)
    }

}