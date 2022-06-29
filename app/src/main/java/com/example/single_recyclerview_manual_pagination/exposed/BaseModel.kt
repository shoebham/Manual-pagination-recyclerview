package com.example.single_recyclerview_manual_pagination.exposed


open class BaseModel<T>(
    open var items: List<T> = emptyList(),
    open var offset: String,
)