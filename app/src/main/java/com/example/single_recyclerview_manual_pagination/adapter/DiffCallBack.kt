package com.example.single_recyclerview_manual_pagination.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.single_recyclerview_manual_pagination.exposed.State
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.exposed.UiModel


class DiffCallBack : DiffUtil.ItemCallback<UiModel<Sticker>>() {
    override fun areItemsTheSame(
        oldItem: UiModel<Sticker>,
        newItem: UiModel<Sticker>
    ): Boolean {
        var returnValue =
            (oldItem is UiModel.Item && newItem is UiModel.Item && oldItem.baseModelOfItem.item?.id == newItem.baseModelOfItem.item?.id) ||
                    (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.category.id == newItem.category.id)
        if (oldItem is UiModel.Item && newItem is UiModel.Item) {
            if (oldItem.baseModelOfItem.item == null && newItem.baseModelOfItem.item == null) {
                if (oldItem.baseModelOfItem.state == State.ERROR || newItem.baseModelOfItem.state == State.ERROR) {
                    returnValue =
                        returnValue && oldItem.baseModelOfItem.state == newItem.baseModelOfItem.state
                }
            }
        }
//        Log.i("diffutil", "areItemsTheSame $returnValue")
        return returnValue
        return (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.category == newItem.category)
    }

    override fun areContentsTheSame(
        oldItem: UiModel<Sticker>,
        newItem: UiModel<Sticker>
    ): Boolean {
        var returnValue =
            (oldItem is UiModel.Item && newItem is UiModel.Item && oldItem.baseModelOfItem.item?.id == newItem.baseModelOfItem.item?.id) ||
                    (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.category.id == newItem.category.id)
//        if (oldItem is UiModel.Item && newItem is UiModel.Item) {
//            if (oldItem.baseModelOfItem.item == null && newItem.baseModelOfItem.item == null) {
//                returnValue =
//                    returnValue && oldItem.baseModelOfItem.state == newItem.baseModelOfItem.state
//            }
//        }
//        Log.i("diffutil", "areContentsTheSame() ${returnValue} ");


//        if (oldItem is UiModel.Item && newItem is UiModel.Item)
//            Log.i(
//                "item_id",
//                "$returnValue old:${oldItem.baseModelOfItem?.item?.id} new:${newItem.baseModelOfItem?.item?.id}"
//            )
//        if ((oldItem is UiModel.Header && newItem is UiModel.Header))
//            Log.i("header", " $returnValue old:${oldItem.text} new:${newItem.text}")

        return returnValue

    }

    override fun getChangePayload(oldItem: UiModel<Sticker>, newItem: UiModel<Sticker>): Any? {
//        Log.i("diffutil", "getChangePayload()");
        return super.getChangePayload(oldItem, newItem)
    }
}