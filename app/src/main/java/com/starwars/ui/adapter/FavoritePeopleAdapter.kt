package com.starwars.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.starwars.R
import com.starwars.room.entity.FavoritePeople
import kotlinx.android.synthetic.main.people_item_adapter.view.*

class FavoritePeopleAdapter(
    private val context: Context,
    var listFavoritePeople: MutableList<FavoritePeople> = mutableListOf(),
    val onFavorite: (FavoritePeople) -> Unit
) : RecyclerView.Adapter<FavoritePeopleAdapter.FavoritePeopleViewHolder>() {

    override fun getItemCount(): Int = listFavoritePeople.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePeopleViewHolder {
        return FavoritePeopleViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.people_item_adapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritePeopleViewHolder, position: Int) {
        holder.bind(listFavoritePeople[position])
    }


    inner class FavoritePeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(people: FavoritePeople) {
            itemView.txtName.text = people.name
            itemView.txtHeight.text = context.getString(R.string.label_height, people.height)
            itemView.txtGender.text = context.getString(R.string.label_gender, people.gender)

            itemView.btnFavorite.setOnClickListener {
                onFavorite(listFavoritePeople[adapterPosition])
            }

            itemView.btnFavorite.setImageResource(R.drawable.ic_favorite)
        }
    }

    fun setList(listFavoritePeople: MutableList<FavoritePeople>) {
        val diffCallback = FavoriteDiffCallback(this.listFavoritePeople, listFavoritePeople)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listFavoritePeople.clear()
        this.listFavoritePeople.addAll(listFavoritePeople)

        diffResult.dispatchUpdatesTo(this)
    }
}