package com.example.single_recyclerview_manual_pagination.exposed

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int)
}

