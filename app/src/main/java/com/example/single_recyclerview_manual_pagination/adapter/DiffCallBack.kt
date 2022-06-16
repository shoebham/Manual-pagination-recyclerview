package com.example.single_recyclerview_manual_pagination.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.UiModel


class DiffCallBack : DiffUtil.ItemCallback<UiModel<Sticker>>() {
    override fun areItemsTheSame(
        oldItem: UiModel<Sticker>,
        newItem: UiModel<Sticker>
    ): Boolean {
        val returnValue =
            (oldItem is UiModel.Item && newItem is UiModel.Item && oldItem.baseModelOfItemInheritingAbstractClass?.item?.id == newItem.baseModelOfItemInheritingAbstractClass?.item?.id) ||
                    (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.text == newItem.text)
        return returnValue
        return (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.text == newItem.text)
    }

    override fun areContentsTheSame(
        oldItem: UiModel<Sticker>,
        newItem: UiModel<Sticker>
    ): Boolean {
        val returnValue =
            (oldItem is UiModel.Item && newItem is UiModel.Item && oldItem.baseModelOfItemInheritingAbstractClass?.item?.id == newItem.baseModelOfItemInheritingAbstractClass?.item?.id) ||
                    (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.text == newItem.text)
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