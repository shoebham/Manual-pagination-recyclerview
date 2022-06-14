package com.example.single_recyclerview_manual_pagination

import android.os.Bundle
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
import com.example.single_recyclerview_manual_pagination.adapter.CustomAdapter
import com.example.single_recyclerview_manual_pagination.adapter.DiffCallBack
import com.example.single_recyclerview_manual_pagination.databinding.ActivityMainBinding
import com.example.single_recyclerview_manual_pagination.models.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var itemList = mutableListOf<String>()
    private lateinit var adapter: CustomAdapter<Sticker>
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


        val emptyListOfCategory = Category<Sticker>()
        val tempList = MutableList(10) { emptyListOfCategory }
        val listContainer = listContainer<Sticker>()
        listContainer.listOfItems = tempList
        baseClass = BaseClass(tempList)
        baseClass.submitList(listContainer, BaseClass.Item_type.ITEM)
        adapter = CustomAdapter(baseClass)
        differ = AsyncListDiffer(adapter, DiffCallBack())
        adapter.differ = differ

//        val templist2 = mutableListOf<StickerUiModel>()
//        val temp = baseClass.convertToUiModelList(tempList)

        adapter.submitList(baseClass.mainList)

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
//            binding.recyclerview.layoutManager.
//            viewModel.categoryList.observe(this) { it ->
//                Log.i("shubham", "data received")
//                baseClass = BaseClass<Sticker>(it)
//                val templist = mutableListOf<UiModel<Sticker>>()
//                var total = 0
//                val newList = mutableListOf<Category<Sticker>>()
//                it.forEach { it2 ->
//                    newList.add(
//                        Category(
//                            id = it2.id,
//                            name = it2.name,
//                            initialCount = it2.initialCount,
//                            itemList = it2.itemList,
//                            currentCount = it2.currentCount,
//                            isViewMoreVisible = it2.isViewMoreVisible
//                        )
//                    )
//                }
////                val bannerList = listOf<Banner>(
////                    Banner("https://business.amazon.com/assets/global/images/success-stories/images/hero/hero-customer-success-stories-uber.png.transform/2880x960/image.jpg"),
////                    Banner("https://business.amazon.com/assets/global/images/success-stories/images/hero/hero-customer-success-stories-uber.png.transform/2880x960/image.jpg"),
////                    Banner("https://business.amazon.com/assets/global/images/success-stories/images/hero/hero-customer-success-stories-uber.png.transform/2880x960/image.jpg")
////                )
//
//                val listContainer = listContainer<Sticker>()
//                listContainer.listOfItems = newList
//                baseClass.submitList(listContainer, BaseClass.Item_type.ITEM)
////                listContainer.listOfBanner=bannerList
////                baseClass.submitList(listContainer,BaseClass.Item_type.BANNER)
//
//                adapter.submitList(baseClass.mainList)
//
//
////                adapter.dataSet=baseClass
//
//            }
        })


    }

    fun convertToBaseModel(stickers: Stickers): List<BaseModelOfItem<Sticker>> {
        val baseModelList = mutableListOf<BaseModelOfItem<Sticker>>()
        stickers.items.forEach {
            baseModelList.add(BaseModelOfItem(it))
        }
        return baseModelList
    }

    fun initItems() {
        lifecycleScope.launch {
            viewModel.getStickers(baseClass).collectLatest {
                adapter.submitList(baseClass.mainList)
            }
        }
    }

    fun getCount() {
        viewModel.count.observe(this, Observer { it ->
            countOfStickers = it
        })
        viewModel.individualCount.observe(this) {
            countMap.putAll(it)
        }
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
//        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                val position = tab.position
//                viewModel.getTabPosition().postValue(position)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
    }



    /**
     * Initialises recyclerview with cat images adapter
     * and also appends loader at the end of list
     *
     */
    private fun initRecyclerView() {
//        adapter = CustomAdapter(baseClass)
//        adapter.setHasStableIds(true)
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


    }

    /**
     * Initialises the tab sync between categories and tabs
     */
    private fun initMediator() {

        tabbedListMediator = TabbedListMediator(
            binding.recyclerview,
            binding.tabs,
            indicesList,
            countMap, false
        )
        tabbedListMediator.attach()

    }
}
