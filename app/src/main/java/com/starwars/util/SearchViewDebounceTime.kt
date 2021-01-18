package com.starwars.util

import android.os.Handler
import androidx.appcompat.widget.SearchView

abstract class SearchViewDebounceTime : SearchView.OnQueryTextListener {
    private val mHandler = Handler()
    override fun onQueryTextSubmit(query: String): Boolean {
        dispatchSearch(query)
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        mHandler.removeCallbacksAndMessages(null)
        mHandler.postDelayed({ dispatchSearch(query) }, 400)
        return true
    }

    abstract fun dispatchSearch(query: String)
}
