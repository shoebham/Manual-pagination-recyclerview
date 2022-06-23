package com.example.single_recyclerview_manual_pagination.exposed

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicInteger


abstract class CustomPagingAdapter<T>
    (
    val dataset: PagingListWrapperClass<T>,
    var apiInterface: ApiInterface<T>
) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    companion object {
        private var bindCount = AtomicInteger(0)
    }

    abstract var differ: AsyncListDiffer<UiModel<T>>

    fun submitList(list: List<UiModel<T>>) {
        dataset.uiModelList = list
        differ.submitList(list)
    }

    fun currentList(): List<UiModel<T>> {
        return differ.currentList
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
//        Log.i("bindcount","bindcount: ${bindCount.incrementAndGet()}")
        val item = currentList()[position]
        holder.bind(item = item, adapter = this@CustomPagingAdapter, position)
    }

    override fun getItemCount() = currentList().size
}