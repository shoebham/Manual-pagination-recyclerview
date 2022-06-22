package com.example.single_recyclerview_manual_pagination.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.example.single_recyclerview_manual_pagination.exposed.AbstractAdapter
import com.example.single_recyclerview_manual_pagination.exposed.ApiInterface
import com.example.single_recyclerview_manual_pagination.exposed.BaseClass
import com.example.single_recyclerview_manual_pagination.exposed.BaseViewHolder
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.exposed.UiModel
import java.util.concurrent.atomic.AtomicInteger

class demoAdapter(
    val baseclass: BaseClass<Sticker>,
    val apiinterface: ApiInterface,
) : AbstractAdapter<Sticker>(baseclass, apiinterface) {

    companion object {
        private var createCount = AtomicInteger(0)

    }

    override lateinit var differ: AsyncListDiffer<UiModel<Sticker>>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Sticker> {
        Log.i("createcount", "createcount: ${createCount.incrementAndGet()}")
        return when (viewType) {
            -1 -> Viewholders.StickerHeaderViewHolder.from(parent)
            0 -> Viewholders.loadmoreviewholder.from(parent)
            else -> Viewholders.StickerViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList()[position]) {
            is UiModel.Header -> -1
            is UiModel.LoadMore -> 0
            else -> 1
        }
    }

}