package com.starwars.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.starwars.R
import com.starwars.retrofit.NetworkState
import com.starwars.retrofit.Status
import com.starwars.retrofit.model.People
import com.starwars.room.database.AppDatabase
import kotlinx.android.synthetic.main.layout_loading.view.*
import kotlinx.android.synthetic.main.people_item_adapter.view.*

class PeopleAdapter(
    private val context: Context,
    val onItemClick: (People) -> Unit,
    val onItemFavorite: (people: People, favorite: Boolean, position: Int) -> Unit
) : PagedListAdapter<People, RecyclerView.ViewHolder>(DiffUtilCallBack()) {

    private var networkState: NetworkState = NetworkState.LOADING
    private var database: AppDatabase? = null

    init {
        database = AppDatabase.getAppDataBase(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.people_item_adapter -> PeopleViewHolder(view)
            R.layout.layout_loading -> NetworkStateItemViewHolder(view)
            else -> {
                throw IllegalArgumentException("Unknown type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (getItemViewType(position)) {
                R.layout.people_item_adapter -> (holder as PeopleViewHolder).bind(it)
                R.layout.layout_loading -> (holder as NetworkStateItemViewHolder).bind(
                    networkState
                )
            }
        }
    }

    inner class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(people: People) {
            val favorite: Boolean =
                database?.favoritePeopleDao()?.getFavoritedPeopleByUrl(people.url) != null

            itemView.txtName.text = people.name
            itemView.txtHeight.text = context.getString(R.string.label_height, people.height)
            itemView.txtGender.text = context.getString(R.string.label_gender, people.gender)

            itemView.setOnClickListener {
                onItemClick(people)
            }

            itemView.btnFavorite.setOnClickListener {
                onItemFavorite(people, !favorite, adapterPosition)
            }

            if (favorite) {
                itemView.btnFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                itemView.btnFavorite.setImageResource(R.drawable.ic_not_favorite)
            }
        }
    }

    inner class NetworkStateItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(networkState: NetworkState) {
            if (networkState.status == Status.RUNNING) {
                itemView.progress_bar.visibility = View.VISIBLE
            } else {
                itemView.progress_bar.visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.layout_loading
        } else {
            R.layout.people_item_adapter
        }
    }

    private fun hasExtraRow(): Boolean = networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = networkState
        val previousExtraRow: Boolean = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow: Boolean = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (!previousExtraRow) {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun updateItem(position: Int, people: People) = notifyItemChanged(position, people)

    class DiffUtilCallBack : DiffUtil.ItemCallback<People>() {
        override fun areItemsTheSame(oldItem: People, newItem: People): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: People, newItem: People): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.url == newItem.url
        }
    }
}
