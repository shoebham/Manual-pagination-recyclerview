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
import com.example.single_recyclerview_manual_pagination.tabSync.TabbedListMediator
import com.example.single_recyclerview_manual_pagination.utils.InjectorUtils
import com.example.single_recyclerview_manual_pagination.viewModel.MainActivityViewModel
import com.example.single_recyclerview_manual_pagination.viewModel.MainActivityViewModelFactory
import com.example.single_recyclerview_manual_pagination.adapter.DiffCallBack
import com.example.single_recyclerview_manual_pagination.adapter.demoAdapter
import com.example.single_recyclerview_manual_pagination.databinding.ActivityMainBinding
import com.example.single_recyclerview_manual_pagination.exposed.ApiInterface
import com.example.single_recyclerview_manual_pagination.exposed.BaseModel
import com.example.single_recyclerview_manual_pagination.exposed.PagingListWrapperClass
import com.example.single_recyclerview_manual_pagination.exposed.UiModel
import com.example.single_recyclerview_manual_pagination.models.*


class MainActivity : AppCompatActivity(), ApiInterface<Sticker> {
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

    private lateinit var pagingListWrapperClass: PagingListWrapperClass<Sticker>
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
        pagingListWrapperClass = PagingListWrapperClass(tempList)
        adapter = demoAdapter(pagingListWrapperClass, this, scope = lifecycleScope)
        differ = AsyncListDiffer(adapter, DiffCallBack())
        adapter.differ = differ
        val uimodellist = pagingListWrapperClass.convertToUiModel()
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
        pagingListWrapperClass.categoryList = viewModel.categoryInheritingAbstractClassList.value!!
        val temp = pagingListWrapperClass.convertToUiModel()

        adapter.submitList(temp)
    }

    fun getCount() {
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
        binding.recyclerview.recycledViewPool.setMaxRecycledViews(0, 1000)
        binding.recyclerview.recycledViewPool.setMaxRecycledViews(1, 1000)
        binding.recyclerview.setItemViewCacheSize(100);
        binding.recyclerview.setNestedScrollingEnabled(false);

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
            ::scrollToCategory,
            ::getCategoryIdxOfCurrentPosition
        )
        tabbedListMediator.attach()

    }

    override suspend fun getItemsWithOffset(
        id: Int,
        offset: String,
        limit: Int
    ): BaseModel<Sticker>? {
        val res = viewModel.getStickersWithOffset(pagingListWrapperClass, id, offset, limit)
        return res
    }

    fun scrollToCategory(id: Int): Int {
        return pagingListWrapperClass.getCategoryPositionInUiModelList(id)
    }

    fun getCategoryIdxOfCurrentPosition(position: Int): Int {
        return pagingListWrapperClass.getCategoryIdxOfCurrentPosition(position)
    }


}
