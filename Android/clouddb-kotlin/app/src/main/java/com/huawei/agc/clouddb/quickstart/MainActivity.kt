/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.agc.clouddb.quickstart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agc.clouddb.quickstart.model.LoginHelper

class MainActivity : AppCompatActivity() {
    private var mViewPager: ViewPager? = null
    private var mNavigationBar: BottomNavigationView? = null
    var loginHelper: LoginHelper? = null
        private set
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        updateTitle()
        if (item.itemId == R.id.navigation_home) {
            if (mViewPager!!.currentItem == Page.HOME.pos) {
                return@OnNavigationItemSelectedListener true
            }
            mViewPager!!.setCurrentItem(Page.HOME.pos, true)
        } else if (item.itemId == R.id.navigation_about_me) {
            if (mViewPager!!.currentItem == Page.ABOUT_ME.pos) {
                return@OnNavigationItemSelectedListener true
            }
            mViewPager!!.setCurrentItem(Page.ABOUT_ME.pos, true)
        }
        true
    }
    private val mPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
        override fun onPageSelected(i: Int) {
            updateTitle()
            if (i == Page.HOME.pos) {
                if (mNavigationBar!!.selectedItemId == R.id.navigation_home) {
                    return
                }
                mNavigationBar!!.selectedItemId = R.id.navigation_home
            } else {
                if (mNavigationBar!!.selectedItemId == R.id.navigation_about_me) {
                    return
                }
                mNavigationBar!!.selectedItemId = R.id.navigation_about_me
            }
        }

        override fun onPageScrollStateChanged(i: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewPager = findViewById(R.id.container)
        mViewPager?.run {
            val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
            adapter = sectionsPagerAdapter
            addOnPageChangeListener(mPageChangeListener)
        }

        mNavigationBar = findViewById(R.id.nav_view)
        mNavigationBar?.run {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }
        loginHelper = LoginHelper(this)
    }

    override fun onResume() {
        updateTitle()
        super.onResume()
    }

    fun hideNavigationBar() {
        mNavigationBar!!.visibility = View.GONE
    }

    fun showNavigationBar() {
        mNavigationBar!!.visibility = View.VISIBLE
    }

    private fun updateTitle() {
        title = if (mViewPager!!.currentItem == Page.HOME.pos) {
            getString(R.string.book_manager_title)
        } else {
            getString(R.string.user_info_str)
        }
    }

    private enum class Page(val pos: Int) {
        HOME(0), ABOUT_ME(1);

    }

    private inner class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return if (position == Page.HOME.pos) {
                HomePageFragment.newInstance()
            } else {
                AboutMeFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return Page.values().size
        }
    }
}
