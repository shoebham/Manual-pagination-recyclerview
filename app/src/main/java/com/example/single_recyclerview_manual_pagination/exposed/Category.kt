package com.example.single_recyclerview_manual_pagination.exposed


/**
 * abstract class for category of type T
 * this class is used to wrap the items inside a category in a recycler view
 * the initial values are expected from the server or client for the module to work
 * @property id the id of the category
 * @property isViewMoreVisible if the view more button is visible or not
 * @property currentCount the current count of the items in the category
 * @property itemsToLoadAfterViewMore the number of items to load after the view more button is clicked
 * @property initialCount the initial count of the items in the category
 * @property baseModelOfItemList the list of items in this category wrapped in BaseModelOfItem
 *
 * @see BaseModelOfItem
 */
abstract class Category<T> {
    abstract val id: Int
    abstract val isViewMoreVisible: Boolean
    abstract val currentCount: Int
    abstract val itemsToLoadAfterViewMore: Int
    abstract val initialCount: Int
    abstract var baseModelOfItemList: List<BaseModelOfItem<T>>
}