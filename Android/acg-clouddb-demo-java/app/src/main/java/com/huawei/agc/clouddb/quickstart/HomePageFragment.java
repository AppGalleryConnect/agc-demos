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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huawei.agc.clouddb.quickstart.model.BookEditFields;
import com.huawei.agc.clouddb.quickstart.model.BookInfo;
import com.huawei.agc.clouddb.quickstart.model.CloudDBZoneWrapper;
import com.huawei.agc.clouddb.quickstart.model.LoginHelper;
import com.huawei.agc.clouddb.quickstart.utils.DateUtils;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HomePageFragment extends Fragment
    implements CloudDBZoneWrapper.UiCallBack, LoginHelper.OnLoginEventCallBack {
    // Request code for clicking search menu
    private static final int REQUEST_SEARCH = 1;

    // Request code for clicking add menu
    private static final int REQUEST_ADD = 2;

    // Store current state of sort order
    private SortState mSortState = new SortState();

    private QueryInfo mQueryInfo = new QueryInfo();

    private CloudDBZoneWrapper mCloudDBZoneWrapper;

    private BookInfoAdapter mBookInfoAdapter;

    private View mHeaderView;

    private View mBottomPanelView;

    private Menu mOptionMenu;

    private boolean mInSearchMode = false;

    private MainActivity mActivity;

    private MyHandler mHandler = new MyHandler();

    public HomePageFragment() {
        mCloudDBZoneWrapper = new CloudDBZoneWrapper();
    }

    static Fragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();
        mBookInfoAdapter = new BookInfoAdapter(getContext());
        mHandler.post(() -> {
            LoginHelper loginHelper = mActivity.getLoginHelper();
            loginHelper.addLoginCallBack(this);
            loginHelper.login();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initBookListHeaderViews(rootView);
        initBookListView(rootView);
        initBottomPanelView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        mOptionMenu = menu;
        inflater.inflate(R.menu.book_manager_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        if (getContext() != null) {
            intent.setClass(getContext(), EditActivity.class);
        }

        int id = item.getItemId();
        if (id == R.id.search_button) {
            intent.setAction(EditActivity.ACTION_SEARCH);
            startActivityForResult(intent, REQUEST_SEARCH);
        } else if (id == R.id.add_button) {
            intent.putExtra(BookEditFields.EDIT_MODE, BookEditFields.EditMode.ADD.name());
            intent.setAction(EditActivity.ACTION_ADD);
            startActivityForResult(intent, REQUEST_ADD);
        } else if (id == R.id.select_all) {
            mBookInfoAdapter.onSelectAllMenuClicked();
            updateSelectAllMenuHint(item);
        } else if (id == R.id.show_all) {
            restoreQueryState();
            mHandler.post(() -> mCloudDBZoneWrapper.queryAllBooks());
            toggleShowAllState(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        restoreQueryState();
        if (requestCode == REQUEST_ADD) {
            processAddAction(data);
        } else {
            processSearchAction(data);
        }
    }

    private void restoreQueryState() {
        mQueryInfo.clean();
        cleanSortState();
    }

    private void processSearchAction(Intent data) {
        CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class);
        String bookName = data.getStringExtra(BookEditFields.BOOK_NAME);
        if (!TextUtils.isEmpty(bookName)) {
            query.contains(BookEditFields.BOOK_NAME, bookName);
            mQueryInfo.bookName = bookName;
        }

        double lowestPrice = data.getDoubleExtra(BookEditFields.LOWEST_PRICE, 0);
        double highestPrice = data.getDoubleExtra(BookEditFields.HIGHEST_PRICE, 0);

        if (lowestPrice > highestPrice) {
            double temp = lowestPrice;
            lowestPrice = highestPrice;
            highestPrice = temp;
        }
        mQueryInfo.lowestPrice = lowestPrice;
        mQueryInfo.highestPrice = highestPrice;

        query.greaterThanOrEqualTo(BookEditFields.PRICE, lowestPrice);
        if (mQueryInfo.lowestPrice != mQueryInfo.highestPrice || mQueryInfo.lowestPrice != 0.0) {
            query.lessThanOrEqualTo(BookEditFields.PRICE, mQueryInfo.highestPrice);
        }

        int showCount = data.getIntExtra(BookEditFields.SHOW_COUNT, 0);
        if (showCount > 0) {
            query.limit(showCount);
            mQueryInfo.showCount = showCount;
        }

        toggleShowAllState(true);
        mHandler.post(() -> mCloudDBZoneWrapper.queryBooks(query));
    }

    private void processAddAction(Intent data) {
        BookInfo bookInfo = new BookInfo();
        int bookID = data.getIntExtra(BookEditFields.BOOK_ID, -1);
        if (bookID == -1) {
            bookInfo.setId(mCloudDBZoneWrapper.getBookIndex() + 1);
        } else {
            bookInfo.setId(bookID);
        }
        bookInfo.setBookName(data.getStringExtra(BookEditFields.BOOK_NAME));
        bookInfo.setAuthor(data.getStringExtra(BookEditFields.AUTHOR));
        bookInfo.setPrice(data.getDoubleExtra(BookEditFields.PRICE, Double.MIN_VALUE));
        bookInfo.setPublisher(data.getStringExtra(BookEditFields.PUBLISHER));
        String dateTime = data.getStringExtra(BookEditFields.PUBLISH_TIME);
        bookInfo.setPublishTime(DateUtils.parseDate(dateTime));
        toggleShowAllState(false);
        mHandler.post(() -> mCloudDBZoneWrapper.upsertBookInfos(bookInfo));
    }

    @Override
    public void onDestroy() {
        mHandler.post(() -> mCloudDBZoneWrapper.closeCloudDBZone());
        super.onDestroy();
    }

    private void initBookListHeaderViews(View rootView) {
        // mHeaderView = inflater.inflate(R.layout.book_manager_header, null);
        mHeaderView = rootView.findViewById(R.id.table_header);

        View bookNameHeaderView = mHeaderView.findViewById(R.id.header_book_name);
        TextView bookNameTitleView = bookNameHeaderView.findViewById(R.id.title_name);
        bookNameTitleView.setText(R.string.book_name);
        bookNameTitleView.setOnClickListener(v -> {
            updateSortState(BookEditFields.BOOK_NAME, bookNameHeaderView);
            queryWithOrder();
        });

        View authorHeaderView = mHeaderView.findViewById(R.id.header_author);
        TextView authorTitleView = authorHeaderView.findViewById(R.id.title_name);
        authorTitleView.setText(R.string.author);
        authorHeaderView.setOnClickListener(v -> {
            updateSortState(BookEditFields.AUTHOR, authorHeaderView);
            queryWithOrder();
        });

        View priceHeaderView = mHeaderView.findViewById(R.id.header_price);
        TextView priceTitleView = priceHeaderView.findViewById(R.id.title_name);
        priceTitleView.setText(R.string.price);
        priceHeaderView.setOnClickListener(v -> {
            updateSortState(BookEditFields.PRICE, priceHeaderView);
            queryWithOrder();
        });

        View borrowerHeaderView = mHeaderView.findViewById(R.id.header_publisher);
        TextView publisherTitleView = borrowerHeaderView.findViewById(R.id.title_name);
        publisherTitleView.setText(R.string.publisher);
        borrowerHeaderView.setOnClickListener(v -> {
            updateSortState(BookEditFields.PUBLISHER, borrowerHeaderView);
            queryWithOrder();
        });

        View publishTimeHeaderView = mHeaderView.findViewById(R.id.header_publish_time);
        TextView publishTimeTitleView = publishTimeHeaderView.findViewById(R.id.title_name);
        publishTimeTitleView.setText(R.string.publish_time);
        publishTimeHeaderView.setOnClickListener(v -> {
            updateSortState(BookEditFields.PUBLISH_TIME, publishTimeHeaderView);
            queryWithOrder();
        });
    }

    private void initBookListView(View rootView) {
        ListView bookListView = rootView.findViewById(R.id.book_list);
        bookListView.setAdapter(mBookInfoAdapter);
        bookListView.setOnItemClickListener((parent, view, position, id) -> {
            if (mBookInfoAdapter.isMultipleMode()) {
                mBookInfoAdapter.check(position);
                updateSelectAllMenuHint(mOptionMenu.findItem(R.id.select_all));
            } else {
                BookInfo bookInfo = (BookInfo) mBookInfoAdapter.getItem(position);
                Intent intent = new Intent();
                if (getContext() != null) {
                    intent.setClass(getContext(), EditActivity.class);
                }
                intent.putExtra(BookEditFields.BOOK_ID, bookInfo.getId());
                intent.putExtra(BookEditFields.BOOK_NAME, bookInfo.getBookName());
                intent.putExtra(BookEditFields.PRICE, bookInfo.getPrice());
                intent.putExtra(BookEditFields.AUTHOR, bookInfo.getAuthor());
                intent.putExtra(BookEditFields.PUBLISHER, bookInfo.getPublisher());
                intent.putExtra(BookEditFields.PUBLISH_TIME, DateUtils.formatDate(bookInfo.getPublishTime()));
                intent.putExtra(BookEditFields.EDIT_MODE, BookEditFields.EditMode.MODIFY.name());
                intent.setAction(EditActivity.ACTION_ADD);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });
        bookListView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (!mBookInfoAdapter.isMultipleMode()) {
                mBookInfoAdapter.setMultipleMode(true);
                invalidateViewInMultipleChoiceMode();
            }
            return false;
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mBookInfoAdapter.isMultipleMode()) {
            invalidateViewInMultipleChoiceMode();
        } else {
            invalidateViewInNormalMode();
        }
    }

    private void initBottomPanelView(View rootView) {
        mBottomPanelView = rootView.findViewById(R.id.buttonPanel);
        Button deleteButton = mBottomPanelView.findViewById(R.id.delete);
        deleteButton.setOnClickListener(v -> {
            List<BookInfo> bookInfoList = mBookInfoAdapter.getSelectedItems();
            mHandler.post(() -> mCloudDBZoneWrapper.deleteBookInfos(bookInfoList));
            invalidateViewInNormalMode();
        });
        Button cancelButton = mBottomPanelView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> invalidateViewInNormalMode());
    }

    private void invalidateViewInMultipleChoiceMode() {
        mHeaderView.findViewById(R.id.checkbox).setVisibility(View.INVISIBLE);
        mBottomPanelView.setVisibility(View.VISIBLE);
        mActivity.hideNavigationBar();

        int menuSize = mOptionMenu.size();
        for (int i = 0; i < menuSize; i++) {
            MenuItem menuItem = mOptionMenu.getItem(i);
            if (menuItem.getItemId() == R.id.select_all) {
                menuItem.setVisible(true);
                menuItem.setTitle(R.string.select_all);
            } else {
                menuItem.setVisible(false);
            }
        }
    }

    private void invalidateViewInNormalMode() {
        mHeaderView.findViewById(R.id.checkbox).setVisibility(View.GONE);
        mBottomPanelView.setVisibility(View.GONE);
        mActivity.showNavigationBar();
        mBookInfoAdapter.setMultipleMode(false);

        int menuSize = mOptionMenu.size();
        for (int i = 0; i < menuSize; i++) {
            MenuItem menuItem = mOptionMenu.getItem(i);
            int id = menuItem.getItemId();
            if (id == R.id.select_all) {
                menuItem.setVisible(false);
            } else if (id == R.id.add_button) {
                menuItem.setVisible(!mInSearchMode);
            } else if (id == R.id.show_all) {
                menuItem.setVisible(mInSearchMode);
            } else {
                menuItem.setVisible(true);
            }
        }
    }

    private void updateSelectAllMenuHint(MenuItem menuItem) {
        if (mBookInfoAdapter.isAllSelected()) {
            menuItem.setTitle(R.string.select_none);
        } else {
            menuItem.setTitle(R.string.select_all);
        }
    }

    private void toggleShowAllState(boolean inSearchMode) {
        mInSearchMode = inSearchMode;
        invalidateViewInNormalMode();
        mOptionMenu.findItem(R.id.show_all).setVisible(mInSearchMode);
    }

    @Override
    public void onAddOrQuery(List<BookInfo> bookInfoList) {
        mHandler.post(() -> {
            // Object is sorted by default encoding, so we need to resort it by pinyin for Chinese
            if (mSortState.state != null && (BookEditFields.BOOK_NAME.equals(mSortState.field)
                || BookEditFields.AUTHOR.equals(mSortState.field) || BookEditFields.PUBLISHER.equals(
                mSortState.field))) {
                Collections.sort(bookInfoList, (o1, o2) -> {
                    Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
                    int result;
                    if (BookEditFields.BOOK_NAME.equals(mSortState.field)) {
                        result = comparator.compare(o1.getBookName(), o2.getBookName());
                    } else if (BookEditFields.AUTHOR.equals(mSortState.field)) {
                        result = comparator.compare(o1.getAuthor(), o2.getAuthor());
                    } else {
                        result = comparator.compare(o1.getPublisher(), o2.getPublisher());
                    }
                    if (mSortState.state == SortState.State.UP) {
                        return result;
                    } else {
                        return -result;
                    }
                });
            }
            mBookInfoAdapter.addBooks(bookInfoList);
        });
    }

    @Override
    public void onSubscribe(List<BookInfo> bookInfoList) {
        if (!mInSearchMode) {
            mHandler.post(() -> mBookInfoAdapter.addBooks(bookInfoList));
        }
    }

    @Override
    public void onDelete(List<BookInfo> bookInfoList) {
        mHandler.post(() -> mBookInfoAdapter.deleteBooks(bookInfoList));
    }

    @Override
    public void updateUiOnError(String errorMessage) {
        // dummy
    }

    private void updateSortState(String field, View view) {
        cleanSortState();

        mSortState.field = field;
        ImageView newDownArrowView = view.findViewById(R.id.arrow_down);
        ImageView newUpArrowView = view.findViewById(R.id.arrow_up);
        if (mSortState.state == SortState.State.UP) {
            mSortState.state = SortState.State.DOWN;
            newDownArrowView.setImageResource(R.drawable.arrow_down_selected);
        } else {
            newUpArrowView.setImageResource(R.drawable.arrow_up_selected);
            mSortState.state = SortState.State.UP;
        }
        mSortState.downArrowView = newDownArrowView;
        mSortState.upArrowView = newUpArrowView;
    }

    private void cleanSortState() {
        mSortState.field = null;
        if (mSortState.upArrowView != null) {
            mSortState.upArrowView.setImageResource(R.drawable.arrow_up);
        }
        if (mSortState.downArrowView != null) {
            mSortState.downArrowView.setImageResource(R.drawable.arrow_down);
        }
    }

    private void queryWithOrder() {
        invalidateViewInNormalMode();
        CloudDBZoneQuery<BookInfo> query = CloudDBZoneQuery.where(BookInfo.class);
        if (!mQueryInfo.bookName.isEmpty()) {
            query.contains(BookEditFields.BOOK_NAME, mQueryInfo.bookName);
        }
        query.greaterThanOrEqualTo(BookEditFields.PRICE, mQueryInfo.lowestPrice);
        if (mQueryInfo.lowestPrice != mQueryInfo.highestPrice || mQueryInfo.lowestPrice != 0.0) {
            query.lessThanOrEqualTo(BookEditFields.PRICE, mQueryInfo.highestPrice);
        }
        if (mQueryInfo.showCount > 0) {
            query.limit(mQueryInfo.showCount);
        }
        if (mSortState.state == SortState.State.UP) {
            query.orderByAsc(mSortState.field);
        } else {
            query.orderByDesc(mSortState.field);
        }
        mHandler.post(() -> mCloudDBZoneWrapper.queryBooks(query));
    }

    @Override
    public void onLogin(boolean showLoginUserInfo, SignInResult signInResult) {
        mHandler.post(() -> {
            mCloudDBZoneWrapper.addCallBacks(HomePageFragment.this);
            mCloudDBZoneWrapper.createObjectType();
            mCloudDBZoneWrapper.openCloudDBZone();
            mCloudDBZoneWrapper.addSubscription();
        });
    }

    @Override
    public void onLogOut(boolean showLoginUserInfo) {
        Toast.makeText(mActivity.getApplicationContext(), "Sign in from agc failed", Toast.LENGTH_SHORT).show();
    }

    private static final class SortState {
        String field;

        ImageView upArrowView;

        ImageView downArrowView;

        State state;

        enum State {
            UP,
            DOWN
        }
    }

    private static final class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // dummy
        }
    }

    private static class BookInfoAdapter extends BaseAdapter {
        private List<BookInfo> mBookList = new ArrayList<>();

        private SparseBooleanArray mCheckState = new SparseBooleanArray();

        private boolean mIsMultipleMode = false;

        private LayoutInflater mInflater;

        BookInfoAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        void addBooks(List<BookInfo> bookInfoList) {
            mBookList.clear();
            mBookList.addAll(bookInfoList);
            notifyDataSetChanged();
        }

        void deleteBooks(List<BookInfo> bookInfoList) {
            for (BookInfo bookInfo : bookInfoList) {
                mBookList.remove(bookInfo);
            }
            notifyDataSetChanged();
        }

        void check(int position) {
            if (mCheckState.get(position)) {
                mCheckState.delete(position);
            } else {
                mCheckState.put(position, true);
            }
            notifyDataSetChanged();
        }

        void onSelectAllMenuClicked() {
            // Current state is all elements selected, so switch the state to none selected
            if (mCheckState.size() == getCount()) {
                mCheckState.clear();
            } else {
                mCheckState.clear();
                for (int i = 0; i < getCount(); i++) {
                    mCheckState.put(i, true);
                }
            }
            notifyDataSetChanged();
        }

        boolean isAllSelected() {
            return mCheckState.size() == mBookList.size();
        }

        List<BookInfo> getSelectedItems() {
            List<BookInfo> bookInfoList = new ArrayList<>();
            int selectCount = mCheckState.size();
            for (int i = 0; i < selectCount; i++) {
                bookInfoList.add(mBookList.get(mCheckState.keyAt(i)));
            }
            return bookInfoList;
        }

        boolean isMultipleMode() {
            return mIsMultipleMode;
        }

        void setMultipleMode(boolean isMultipleMode) {
            mIsMultipleMode = isMultipleMode;
            if (!isMultipleMode) {
                mCheckState.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mBookList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBookList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.book_list_item, null);
                viewHolder.authorView = convertView.findViewById(R.id.author);
                viewHolder.bookNameView = convertView.findViewById(R.id.book_name);
                viewHolder.priceView = convertView.findViewById(R.id.price);
                viewHolder.publisherView = convertView.findViewById(R.id.publisher);
                viewHolder.publishTimeView = convertView.findViewById(R.id.publish_time);
                viewHolder.checkBox = convertView.findViewById(R.id.checkbox);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BookInfo bookInfo = mBookList.get(position);
            viewHolder.authorView.setText(bookInfo.getAuthor());
            viewHolder.bookNameView.setText(bookInfo.getBookName());
            viewHolder.priceView.setText(String.format(Locale.getDefault(), "%.2f", bookInfo.getPrice()));
            viewHolder.publisherView.setText(bookInfo.getPublisher());
            viewHolder.publishTimeView.setText(DateUtils.formatDate(bookInfo.getPublishTime()));
            if (mIsMultipleMode) {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            } else {
                viewHolder.checkBox.setVisibility(View.GONE);
            }
            viewHolder.checkBox.setChecked(mCheckState.get(position, false));
            return convertView;
        }
    }

    private static final class ViewHolder {
        CheckBox checkBox;

        TextView bookNameView;

        TextView authorView;

        TextView priceView;

        TextView publisherView;

        TextView publishTimeView;
    }

    private static final class QueryInfo {
        String bookName = "";

        double lowestPrice = 0.0;

        double highestPrice = 0.0;

        int showCount = 0;

        void clean() {
            bookName = "";
            lowestPrice = 0.0;
            highestPrice = 0.0;
            showCount = 0;
        }
    }
}
