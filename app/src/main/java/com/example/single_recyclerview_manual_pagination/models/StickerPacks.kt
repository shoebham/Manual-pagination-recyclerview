package com.example.single_recyclerview_manual_pagination.models



data class StickerPacks(
    val stickerPacks: MutableList<StickerPack>,
)

data class StickerPack(
    var id: Int,
    var name: String = "",
    
    
    
    
    
    
    var total: Int = 0
)

data class Icon(
    val height: Int,
    val png: Png,
    val width: Int
)
