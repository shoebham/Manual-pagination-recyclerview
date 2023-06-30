package com.example.single_recyclerview_manual_pagination.network

import com.example.single_recyclerview_manual_pagination.models.StickerPacks
import com.example.single_recyclerview_manual_pagination.models.Stickers
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL =
    "your-api"

interface ApiService {
    @GET("/{id}/stickersEndpoint")
    suspend fun getStickers(
        @Path("id") id: Int,
        @Query("offset") offset: String? = null,
        @Query("limit") limit: Int? = null
    ): Stickers

    @GET("/stickerPackEndpoint")
    suspend fun getStickerPacks(
    ): StickerPacks

}
