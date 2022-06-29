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
class BaseModelOfItem<T>(
    val item: T? = null,
    val categoryBasedPosition: Int = 0,
    val continuousPosition: Int = 0,
    var isLastItem: Boolean = false,
    var category: Category<T>? = null,
    var state: State = State.NOT_LOADING,
    var isLoadMoreClicked: Boolean = false,
    val offset: String = "1",
)


