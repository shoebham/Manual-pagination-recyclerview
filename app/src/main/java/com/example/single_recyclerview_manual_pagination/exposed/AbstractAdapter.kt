package com.example.single_recyclerview_manual_pagination.exposed

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicInteger

interface ApiInterface {
    fun getItemsWithOffset(id: Int, offset: String, limit: Int)
    fun scrollToCategory(id: Int?): Int
    fun getCategoryIdxOfCurrentPosition(position: Int): Int
}

abstract class AbstractAdapter<T>
    (
    val dataset: BaseClass<T>,
    var apiInterface: ApiInterface
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
        holder.bind(item = item, adapter = this, position)
    }

    override fun getItemCount() = currentList().size
}