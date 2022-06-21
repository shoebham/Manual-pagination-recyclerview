package com.example.single_recyclerview_manual_pagination

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.***REMOVED***_vertical_scroll_stickers.tabSync.TabbedListMediator
import com.example.***REMOVED***_vertical_scroll_stickers.utils.InjectorUtils
import com.example.***REMOVED***_vertical_scroll_stickers.viewModel.MainActivityViewModel
import com.example.***REMOVED***_vertical_scroll_stickers.viewModel.MainActivityViewModelFactory
import com.example.single_recyclerview_manual_pagination.adapter.DiffCallBack
import com.example.single_recyclerview_manual_pagination.adapter.demoAdapter
import com.example.single_recyclerview_manual_pagination.databinding.ActivityMainBinding
import com.example.single_recyclerview_manual_pagination.exposed.ApiInterface
import com.example.single_recyclerview_manual_pagination.exposed.BaseClass
import com.example.single_recyclerview_manual_pagination.models.*
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), ApiInterface {
    private var itemList = mutableListOf<String>()
    private lateinit var adapter: demoAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var tabbedListMediator: TabbedListMediator
    private lateinit var indicesList: HashMap<Int, Int>
    private lateinit var categoryList: StickerPacks
    private lateinit var countMap: LinkedHashMap<String, Int>
    private var countOfStickers: Int = 0
    private var countList: MutableList<Int> = ArrayList()

        private lateinit var baseClass: BaseClass<Sticker>
    private lateinit var differ: AsyncListDiffer<UiModel<Sticker>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        for (i in 1..1000) {
            itemList.add("Item $i");
        }
        initUi()


        val emptyListOfCategory = CategoryInheritingAbstractClass()
        val tempList = MutableList(10) { emptyListOfCategory }
        baseClass = BaseClass(tempList)

        adapter = demoAdapter(baseClass, this)
        differ = AsyncListDiffer(adapter, DiffCallBack())
        adapter.differ = differ


        val uimodellist = baseClass.convertToUiModel()
        adapter.submitList(uimodellist)
//        adapter.setApiListener(this)
    }
    fun initUi() {
        factory = InjectorUtils.provideMainActivityViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)
            .get(MainActivityViewModel::class.java)
        indicesList = HashMap<Int, Int>()

        viewModel.stickerPacks.observe(this, Observer { item ->
            categoryList = item

            for ((i, stickerpack) in categoryList.stickerPacks.withIndex()) {
                indicesList.put(stickerpack.id, i)
            }
            countMap = LinkedHashMap<String, Int>()
            getCount()
            initRecyclerView()
            initItems()
            initTabLayout()
            initMediator()

        })


    }

    fun initItems() {
        baseClass.listOfItems = viewModel.categoryInheritingAbstractClassList.value!!
        val temp = baseClass.convertToUiModel()
        adapter.submitList(temp)
    }

    fun getCount() {
//        viewModel.count.observe(this, Observer { it ->
//            countOfStickers = it
//        })
//        viewModel.individualCount.observe(this) {
//            countMap.putAll(it)
//        }
        Log.i("mainactivityy", "countofstickers${countOfStickers} ${countMap.size}")
        for (key in countMap.keys) {
            Log.i("countmap", "key:${key} value:${countMap.get(key)}")
        }
    }

    /**
     * Initialises tab layout after getting category in the first call
     */
    fun initTabLayout() {
        if (categoryList.stickerPacks.isEmpty()) {
            binding.tabs.addTab((binding.tabs.newTab().setText("")))
        } else {
            for (category in categoryList.stickerPacks) {
                binding.tabs.addTab(binding.tabs.newTab().setText(category.name))
            }
        }

    }


    /**
     * Initialises recyclerview with cat images adapter
     * and also appends loader at the end of list
     *
     */
    lateinit var recyclerViewState: Parcelable
    private fun initRecyclerView() {
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(this, 3)
        (binding.recyclerview.layoutManager as GridLayoutManager).setSpanSizeLookup(object :
            GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == adapter.itemCount)
                    return 3
                return if (adapter.getItemViewType(position) <= 0) 3
                else 1
            }
        })
        binding.recyclerview.itemAnimator = null
        recyclerViewState = binding.recyclerview.getLayoutManager()?.onSaveInstanceState()!!;
    }

    /**
     * Initialises the tab sync between categories and tabs
     */
    private fun initMediator() {

        tabbedListMediator = TabbedListMediator(
            binding.recyclerview,
            binding.tabs,
            indicesList,
            countMap,
            false,
            viewModel,
            ::scrollToCategory
        )
        tabbedListMediator.attach()

    }

    override fun getItemsWithOffset(id: Int, offset: String, limit: Int) {
        lifecycleScope.launch {
            viewModel.getStickersWithOffset(baseClass, id, offset, limit).collectLatest {
                adapter.submitList(it)
                binding.recyclerview.getLayoutManager()?.onRestoreInstanceState(recyclerViewState);


            }
        }
    }

    override fun scrollToCategory(id: Int?): Int {
        Log.i("scrolltocategory", "here $id")
        for ((i, item) in baseClass.uiModelList.withIndex()) {
            if (item is UiModel.Header) {
                if (item.category?.id == id)
                    return i
            }
        }
        return 0
    }

//    override fun <T> convertToUiModel(
//        baseClass: BaseClass<T>,
//        categoryInheritingAbstractClassList: List<CategoryInheritingAbstractClass<T>>
//    ): List<UiModel<T>> {
//        val uiModelList = mutableListOf<UiModel<T>>()
//        for ((i, item) in categoryInheritingAbstractClassList.withIndex()) {
//            for ((j, it) in item.itemInheritingAbstractClassList.withIndex()) {
//                it.category = item
//                uiModelList.add(UiModel.Item(it))
//            }
//            uiModelList.add(UiModel.Header(item.name))
//            uiModelList.add(
//                UiModel.LoadMore(
//                    itemInheritingAbstractClassAbove = item.itemInheritingAbstractClassList.last(),
//                    id = item.id,
//                    visible = item.isViewMoreVisible
//                )
//            )
//        }
//        baseClass.uiModelList = uiModelList
//        return baseClass.uiModelList
//    }
}
