package com.example.single_recyclerview_manual_pagination.exposed

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.single_recyclerview_manual_pagination.exposed.BaseViewHolder
import com.example.single_recyclerview_manual_pagination.models.BaseClass
import com.example.single_recyclerview_manual_pagination.models.CategoryInheritingAbstractClass
import com.example.single_recyclerview_manual_pagination.models.UiModel

interface ApiInterface {
    fun getItemsWithOffset(id: Int, offset: String, limit: Int)
    fun <T> convertToUiModel(
        baseClass: BaseClass<T>,
        categoryInheritingAbstractClassList: List<CategoryInheritingAbstractClass<T>>
    ): List<UiModel<T>>
}

abstract class AbstractAdapter<T>
    (
    val dataset: BaseClass<T>,
    val apiInterface: ApiInterface
) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    abstract var differ: AsyncListDiffer<UiModel<T>>

    fun submitList(list: List<UiModel<T>>) {
        dataset.uiModelList = list
        differ.submitList(list)
    }

    private fun currentList(): List<UiModel<T>> {
        return differ.currentList
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = currentList().get(position)
        holder.bind(item = item, adapter = this, position)
    }

    override fun getItemCount() = currentList().size
}