package com.example.single_recyclerview_manual_pagination.adapter

import com.example.single_recyclerview_manual_pagination.models.Category
import com.example.single_recyclerview_manual_pagination.models.Wrapper

class listContainer<T>() : Wrapper<T>() {
    override var listOfItems: List<Category<T>> = emptyList()

}