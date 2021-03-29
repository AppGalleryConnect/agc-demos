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

package com.huawei.agc.clouddb.quickstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.huawei.agc.clouddb.quickstart.model.LoginHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    private BottomNavigationView mNavigationBar;

    private LoginHelper mLoginHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            updateTitle();
            if (item.getItemId() == R.id.navigation_home) {
                if (mViewPager.getCurrentItem() == Page.HOME.getPos()) {
                    return true;
                }
                mViewPager.setCurrentItem(Page.HOME.getPos(), true);
            } else if (item.getItemId() == R.id.navigation_about_me) {
                if (mViewPager.getCurrentItem() == Page.ABOUT_ME.getPos()) {
                    return true;
                }
                mViewPager.setCurrentItem(Page.ABOUT_ME.getPos(), true);
            }
            return true;
        }
    };

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            updateTitle();
            if (i == Page.HOME.getPos()) {
                if (mNavigationBar.getSelectedItemId() == R.id.navigation_home) {
                    return;
                }
                mNavigationBar.setSelectedItemId(R.id.navigation_home);
            } else {
                if (mNavigationBar.getSelectedItemId() == R.id.navigation_about_me) {
                    return;
                }
                mNavigationBar.setSelectedItemId(R.id.navigation_about_me);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.container);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);

        mNavigationBar = findViewById(R.id.nav_view);
        mNavigationBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mLoginHelper = new LoginHelper(this);
    }

    @Override
    protected void onResume() {
        updateTitle();
        super.onResume();
    }

    protected LoginHelper getLoginHelper() {
        return mLoginHelper;
    }

    protected void hideNavigationBar() {
        mNavigationBar.setVisibility(View.GONE);
    }

    protected void showNavigationBar() {
        mNavigationBar.setVisibility(View.VISIBLE);
    }

    private void updateTitle() {
        if (mViewPager.getCurrentItem() == Page.HOME.getPos()) {
            setTitle(getString(R.string.book_manager_title));
        } else {
            setTitle(getString(R.string.user_info_str));
        }
    }

    private enum Page {
        HOME(0),
        ABOUT_ME(1);

        private final int pos;

        Page(int pos) {
            this.pos = pos;
        }

        int getPos() {
            return pos;
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == Page.HOME.pos) {
                return HomePageFragment.newInstance();
            } else {
                return AboutMeFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return Page.values().length;
        }
    }
}
