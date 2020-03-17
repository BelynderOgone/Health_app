package com.example.myapplication.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*
//This adapter helps in organising tabs in admin page, creates a stack for swiping tabs

class TabbedAdminPageAdapter(fm:FragmentManager): FragmentStatePagerAdapter(fm){
    private val fragmentList:MutableList<Fragment> = ArrayList()
    private val fragmentTitleList:MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    fun addFragment(fragment:Fragment,title:String){
        fragmentList.add(fragment)
        fragmentTitleList.add(title)

    }
}

