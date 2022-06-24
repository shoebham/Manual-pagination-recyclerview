package com.example.single_recyclerview_manual_pagination.exposed

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * BaseViewHolder for different types of ViewHolders in the recycler view.
 */
abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Binds the data to the view holder.
     * @param item The data to bind to the view holder.
     * @param adapter The adapter that is binding the data.
     * @param position The position of the item in the list.
     */
    abstract fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int)
}

