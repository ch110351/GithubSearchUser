package com.example.githubsearchuser.widget


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class DataBindingPagingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val itemClick: ItemClick<T>? = null
) :
    PagedListAdapter<T, DataBindingViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int): Unit =
        position?.let {
            getItem(position)?.let { it1 -> holder.bind(it1) }
        }
}


class DataBindingViewHolder<T>(
    private val binding: ViewDataBinding,
    private val itemClick: ItemClick<T>? = null
) :
    RecyclerView.ViewHolder(binding.root) {
    constructor(binding: ViewDataBinding) : this(binding, null)

    fun bind(item: T) {
        binding.setVariable(BR.item, item)
        if (itemClick != null)
            binding.setVariable(BR.itemclick, itemClick)
        binding.executePendingBindings()
    }
}