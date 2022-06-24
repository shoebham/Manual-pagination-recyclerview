package com.example.single_recyclerview_manual_pagination.exposed


/**
 * a abstract UIModel class that is used to represent a UI model
 * User can inherit this and define more items if needed
 * the rest are required for the module to work
 */
abstract class UiModel<T> {
    /**
     * the type of the item
     * @param baseModelOfItem base model of the item
     */
    class Item<T>(val baseModelOfItem: BaseModelOfItem<T>) : UiModel<T>()

    /**
     * Header of the list
     * @param category usually header is derived from category's property so category is stored here
     */
    class Header<T>(val category: Category<T>) : UiModel<T>()

    /**
     * Load more in the list
     * @param id the id of the category in which load more is present
     * @param baseModelItemAbove the item just above loadmore item so that its properties can help in sending the correct api call
     * @param visible if the load more is visible or not
     */
    class LoadMore<T>(
        val id: Int? = null,
        val baseModelItemAbove: BaseModelOfItem<T>,
        var visible: Boolean = true
    ) : UiModel<T>()
}
