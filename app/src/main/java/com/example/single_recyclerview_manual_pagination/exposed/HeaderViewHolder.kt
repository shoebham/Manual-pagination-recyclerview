package com.example.single_recyclerview_manual_pagination.exposed

import androidx.viewbinding.ViewBinding


abstract class HeaderViewHolder<T>(binding: ViewBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int) {
        bindHeader(item as UiModel.Header<T>, adapter, position)
    }

    abstract fun bindHeader(
        header: UiModel.Header<T>,
        adapter: CustomPagingAdapter<T>,
        position: Int
    )
}