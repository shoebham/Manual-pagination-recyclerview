package com.example.single_recyclerview_manual_pagination.exposed


/**
 * A base model class for Item with default values.
 * Default values are used for placeholders
 * Whenever the data is returned in List<T> it gets wrapped to BaseModelOfItem<T>
 * @param item The item to be wrapped
 * @param categoryBasedPosition The position of the item in the category
 * @param continuousPosition The position of the item in the continuous list of items
 * which is used in recyclerview
 * @param isLastItem Whether the item is the last item in the category
 * @param category The category of the item
 * @param state The state of the item
 * @param isLoadMoreClicked indicates whether this item is after the load more button is clicked
 * this helps in sending api call with correct parameters ex. when load more is clicked the limit
 * is changed to itemsToLoadAfterViewMore parameter which is extracted from the category
 */
open class BaseModelOfItem<T>(
    open val item: T? = null,
    open val categoryBasedPosition: Int = 0,
    open val continuousPosition: Int = 0,
    open var isLastItem: Boolean = false,
    open var category: Category<T>? = null,
    open var state: State = State.NOT_LOADING,
    open var isLoadMoreClicked: Boolean = false
)


