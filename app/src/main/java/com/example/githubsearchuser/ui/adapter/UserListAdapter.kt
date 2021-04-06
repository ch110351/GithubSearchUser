package com.example.githubsearchuser.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.githubsearchuser.widget.DataBindingPagingAdapter
import com.example.githubsearchuser.widget.ItemClick
import com.example.githubsearchuser.R
import com.example.githubsearchuser.data.UserItem

class UserListAdapter(itemClick: ItemClick<UserItem>?) :
    DataBindingPagingAdapter<UserItem>(
        DiffCallback(), itemClick
    ) {
    constructor() : this(null)

    class DiffCallback : DiffUtil.ItemCallback<UserItem>() {

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: UserItem,
            newItem: UserItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: UserItem,
            newItem: UserItem
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_user
}