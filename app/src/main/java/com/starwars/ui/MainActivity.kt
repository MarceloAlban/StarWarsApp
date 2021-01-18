package com.starwars.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.starwars.R
import com.starwars.retrofit.Status
import com.starwars.retrofit.model.People
import com.starwars.ui.adapter.PeopleAdapter
import com.starwars.util.SearchViewDebounceTime
import com.starwars.util.showCustomSnackBar
import com.starwars.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.android.synthetic.main.layout_no_internet.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_FAVORITES_ACTIVITY: Int = 1
    }

    private lateinit var adapter: PeopleAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configureAdapter()
        configureObserver()
        configureSwipeRefreshLayout()
    }

    /**
     * On tap in a person
     * */
    private fun onItemClick(people: People) {
        val intent = Intent(this, PeopleDetailActivity::class.java)
        intent.putExtra(PeopleDetailActivity.DATA_EXTRA, people)
        startActivity(intent)
    }

    /**
     * On favorite a person
     * */
    private fun onItemFavorite(people: People, favorite: Boolean, position: Int) {
        viewModel.favoritePeople(people, favorite).observe(this, Observer {
            adapter.updateItem(position, people)

            if (favorite) {
                showCustomSnackBar(
                    recyclerView,
                    getString(R.string.message_item_favorited, people.name)
                )
            } else {
                showCustomSnackBar(
                    recyclerView,
                    getString(R.string.message_item_remove_favorite, people.name)
                )
            }
        })
    }

    private fun configureAdapter() {
        adapter = PeopleAdapter(this,
            { people -> onItemClick(people) },
            { people, favorite, position ->
                onItemFavorite(people, favorite, position)
            })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun configureObserver() {
        viewModel.getPeople().observe(this, Observer { list ->
            adapter.submitList(list)
        })

        viewModel.getInitialStateLiveData().observe(this, Observer { networkState ->
            when (networkState.status) {
                Status.SUCCESS -> {
                    progress_bar.visibility = View.GONE
                    no_internet.visibility = View.GONE
                    swipeRefresh.isRefreshing = false
                }
                Status.FAILED -> {
                    progress_bar.visibility = View.GONE
                    no_internet.visibility = View.VISIBLE
                    swipeRefresh.isRefreshing = false
                }
                Status.RUNNING -> {
                    no_internet.visibility = View.GONE
                    if (!swipeRefresh.isRefreshing) {
                        progress_bar.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.getPaginatedStateLiveData().observe(this, Observer { list ->
            adapter.setNetworkState(list)
        })
    }

    private fun configureSwipeRefreshLayout() {
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val actionItemSearch: MenuItem = menu.findItem(R.id.menu_action_search)
        val searchView = actionItemSearch.actionView as SearchView
        searchView.queryHint = getString(R.string.menu_search)
        searchView.setOnQueryTextListener(
            object : SearchViewDebounceTime() {
                override fun dispatchSearch(query: String) {
                    if (query.isNotEmpty()) {
                        viewModel.searchByName(query)
                    }
                }
            })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_favorite) {
            startActivityForResult(
                Intent(this, FavoriteActivity::class.java),
                REQUEST_FAVORITES_ACTIVITY
            )
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FAVORITES_ACTIVITY) {
            adapter.notifyDataSetChanged()
        }
    }
}
