package com.example.***REMOVED***_vertical_scroll_stickers.tabSync

import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

class TabViewCompositeClickListener(private val mTabLayout: TabLayout) {

    private val listeners: MutableList<(tab: TabLayout.Tab, position: Int) -> Unit> = ArrayList()

    fun addListener(listener: (tab: TabLayout.Tab, position: Int) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (tab: TabLayout.Tab, position: Int) -> Unit) {
        listeners.remove(listener)
    }

    fun build() {
        for (i in 0 until mTabLayout.tabCount) {
            mTabLayout.getTabAt(i)!!.view.setOnClickListener {
                for (listener in listeners) {
                    listener(mTabLayout.getTabAt(i)!!, i)
                }
            }
        }
    }

    fun getListeners(): List<(tab: TabLayout.Tab, position: Int) -> Unit> {
        return listeners
    }
}