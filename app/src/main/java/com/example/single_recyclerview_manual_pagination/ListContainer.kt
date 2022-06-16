package com.example.single_recyclerview_manual_pagination

import com.example.single_recyclerview_manual_pagination.models.Banner
import com.example.single_recyclerview_manual_pagination.models.CategoryInheritingAbstractClass
import com.example.single_recyclerview_manual_pagination.models.Wrapper

class listContainer<T>() : Wrapper<T>() {
    override var listOfItems: List<CategoryInheritingAbstractClass<T>> = emptyList()
    override var listOfBanner: List<Banner> = emptyList()
}