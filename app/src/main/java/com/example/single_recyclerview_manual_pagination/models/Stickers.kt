package com.example.single_recyclerview_manual_pagination.models

import com.example.single_recyclerview_manual_pagination.exposed.BaseModel
import com.squareup.moshi.Json

data class Stickers(
    @Json(name = "stickers")
    override var items: List<Sticker>,
    override var offset: String
) : BaseModel<Sticker>(offset = offset)

data class Sticker(
    val backgroundColor: String?=null,
    val fixedWidthFull: FixedWidthFull?=null,
    val fixedWidthMedium: FixedWidthMedium?=null,
    val fixedWidthSmall: FixedWidthSmall?=null,
    val fixedWidthTiny: FixedWidthTiny?=null,
    val gender: String?=null,
    val id: Int?=null,
    val shareURL: String?=null,
    var categoryName: String? = null,
    val categoryPosition:Int?=null
)

data class Png(
    val url: String,
    val withoutTextURL: String? = null
)
data class FixedWidthFull(
    @Json(name = "png")
    val png: Png,
    val height: Int,
    val width: Int
)

data class FixedWidthMedium(
    @Json(name = "png")
    val png: Png,
    val height: Int,
    val width: Int
)

data class FixedWidthSmall(
    @Json(name = "png")
    val png: Png,
    val height: Int,
    val width: Int
)

data class FixedWidthTiny(
    @Json(name = "png")
    val png: Png,
    val height: Int,
    val width: Int
)

data class WatermarkDetails(
    val url: String
)

