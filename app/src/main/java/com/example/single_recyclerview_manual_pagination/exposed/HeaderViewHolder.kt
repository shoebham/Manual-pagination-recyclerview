package com.example.single_recyclerview_manual_pagination.exposed

import androidx.viewbinding.ViewBinding


/**
 * Abstract class to provide support for headers in vertical scrolling recycler view.
 * Assumes the support of view binding
 */
abstract class HeaderViewHolder<T>(binding: ViewBinding) :
    BaseViewHolder<T>(binding.root) {
    /**
     * Binds the data to the view holder.
     */
    override fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int) {
        bindHeader(item as UiModel.Header<T>)
    }

    /**
     * User defined method to bind the data to the view holder.
     * @param header the header class of UIModel type
     * user should cast the category inside the header class to the child class that user
     * implemented to access additional properties defined by user
     * for ex. if user implemented a class childCategory extending [Category] class and
     * adds name property in child class then to access name here use
     * (header.category as childCategory).name
     */
    abstract fun bindHeader(
        header: UiModel.Header<T>
    )
}