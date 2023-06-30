package com.example.single_recyclerview_manual_pagination.models

import com.example.single_recyclerview_manual_pagination.exposed.BaseModel
import com.squareup.moshi.Json

data class Stickers(
    @Json(name = "stickers")
    override var items: List<Sticker>,
    override var offset: String
) : BaseModel<Sticker>(offset = offset)

data class Sticker(
    val fixedWidthTiny: FixedWidthTiny?=null,
    val id: Int?=null,
)

data class Png(
    val url: String,
    val withoutTextURL: String? = null
)


data class FixedWidthTiny(
    @Json(name = "png")
    val png: Png,
    val height: Int,
    val width: Int
)


