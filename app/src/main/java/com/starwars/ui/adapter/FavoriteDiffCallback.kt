package com.starwars.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.starwars.room.entity.FavoritePeople

class FavoriteDiffCallback(
    private val oldList: List<FavoritePeople>,
    private val newList: List<FavoritePeople>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.name == newItem.name && oldItem.url == newItem.url
    }
}