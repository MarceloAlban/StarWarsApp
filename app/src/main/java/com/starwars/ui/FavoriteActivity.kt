package com.starwars.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.starwars.R
import com.starwars.ui.adapter.FavoritePeopleAdapter
import com.starwars.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_no_favorites.*
import kotlinx.android.synthetic.main.toolbar.*

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoritePeopleAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        configureAdapter()
        configureObserver()
    }

    private fun configureAdapter() {
        adapter = FavoritePeopleAdapter(this) { favoritePeople ->
            viewModel.deleteFavorite(favoritePeople)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun configureObserver() {
        viewModel.getAll()?.observe(this, Observer { list ->
            no_data.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

            adapter.setList(list)
        })
    }
}
