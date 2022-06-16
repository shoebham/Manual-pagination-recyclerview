package com.example.single_recyclerview_manual_pagination.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.example.single_recyclerview_manual_pagination.exposed.AbstractAdapter
import com.example.single_recyclerview_manual_pagination.exposed.ApiInterface
import com.example.single_recyclerview_manual_pagination.exposed.BaseClass
import com.example.single_recyclerview_manual_pagination.exposed.BaseViewHolder
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.UiModel

class demoAdapter(
    val baseclass: BaseClass<Sticker>,
    val apiinterface: ApiInterface,
) : AbstractAdapter<Sticker>(baseclass, apiinterface) {

    override lateinit var differ: AsyncListDiffer<UiModel<Sticker>>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Sticker> {
        return when (viewType) {
            -1 -> Viewholders.StickerHeaderViewHolder.from(parent)
            0 -> Viewholders.loadmoreviewholder.from(parent)
            else -> Viewholders.StickerViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Use peek over getItem to avoid triggering page fetch / drops, since
        // recycling views is not indicative of the user's current scroll position.
        return when (currentList()[position]) {
            is UiModel.Header -> -1
            is UiModel.LoadMore -> 0
            else -> 1
        }
    }

}