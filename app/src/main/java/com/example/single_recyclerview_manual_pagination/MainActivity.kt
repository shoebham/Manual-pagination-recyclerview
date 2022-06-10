package com.example.single_recyclerview_manual_pagination

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.***REMOVED***_vertical_scroll_stickers.tabSync.TabbedListMediator
import com.example.***REMOVED***_vertical_scroll_stickers.utils.InjectorUtils
import com.example.***REMOVED***_vertical_scroll_stickers.viewModel.MainActivityViewModel
import com.example.***REMOVED***_vertical_scroll_stickers.viewModel.MainActivityViewModelFactory
import com.example.single_recyclerview_manual_pagination.databinding.ActivityMainBinding
import com.example.single_recyclerview_manual_pagination.models.BaseClass
import com.example.single_recyclerview_manual_pagination.models.Category
import com.example.single_recyclerview_manual_pagination.models.StickerPacks
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var itemList = mutableListOf<String>()
    private lateinit var adapter:CustomAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var tabbedListMediator: TabbedListMediator
    private lateinit var indicesList: HashMap<Int, Int>
    private lateinit var categoryList: StickerPacks
    private lateinit var countMap: LinkedHashMap<String, Int>
    private var countOfStickers: Int = 0
    private var countList: MutableList<Int> = ArrayList()
    private lateinit var baseClass: BaseClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        for (i in 1..1000) {
            itemList.add("Item $i");
        }
        initUi()
        val emptyListOfCategory = Category()
        val tempList = mutableListOf<Category>(emptyListOfCategory)
        baseClass = BaseClass(tempList)
        adapter = CustomAdapter(baseClass)

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

            viewModel.categoryList.observe(this) {
                baseClass = BaseClass(it)
//                adapter.dataSet=baseClass
                adapter.submitList(it)
            }
            initTabLayout()
            initMediator()
        })


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
        adapter = CustomAdapter(baseClass)
//        adapter.setHasStableIds(true)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(this, 3)
        (binding.recyclerview.layoutManager as GridLayoutManager).setSpanSizeLookup(object :
            GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position == adapter.itemCount)
                    return 3
                return if (adapter.getItemViewType(position) < 0) 3
                else 1
            }
        })
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
