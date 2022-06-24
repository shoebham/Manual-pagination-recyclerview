package com.example.single_recyclerview_manual_pagination.exposed

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicInteger

/**
 * A custom adapter that will be used for custom pagination support
 * @param dataset the wrapper list which contains category list and UIModel List which is used in recyclerview
 * @param apiInterface the api interface which is used to fetch data from server
 * @property differ the AsyncListDiffer which is used to calculate differences between old list and new list
 */
abstract class CustomPagingAdapter<T>
    (
    val dataset: PagingListWrapperClass<T>,
    var apiInterface: ApiInterface<T>
) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    companion object {
        private var bindCount = AtomicInteger(0)
    }

    abstract var differ: AsyncListDiffer<UiModel<T>>

    /**
     * Submit the new list to be diffed and displayed.
     */
    fun submitList(list: List<UiModel<T>>) {
        dataset.uiModelList = list
        differ.submitList(list)
    }

    /**
     * get the current list of UiModel
     */
    fun currentList(): List<UiModel<T>> {
        return differ.currentList
    }

    /**
     * Calls the respective bind function of the correct view holder based on the view type
     */
    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = currentList()[position]
        holder.bind(item = item, adapter = this@CustomPagingAdapter, position)
    }

    /**
     * Returns the size of the current UI model list
     */
    override fun getItemCount() = currentList().size
}