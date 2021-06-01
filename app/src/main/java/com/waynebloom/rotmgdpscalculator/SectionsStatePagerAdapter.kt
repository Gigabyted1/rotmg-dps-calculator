package com.waynebloom.rotmgdpscalculator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class SectionsStatePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    fun addFragment(fragment: Fragment, fragmentTitle: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(fragmentTitle)
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun getFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }
}