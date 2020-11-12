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

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huawei.agc.clouddb.quickstart.model.BookEditFields;
import com.huawei.agc.clouddb.quickstart.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    static final String ACTION_ADD = "com.huawei.agc.clouddb.quickstart.ADD";

    static final String ACTION_SEARCH = "com.huawei.agc.clouddb.quickstart.SEARCH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        String action = intent.getAction();
        EditText bookNameEdit = findViewById(R.id.edit_bookname);

        Button addButton = findViewById(R.id.add);
        Button searchButton = findViewById(R.id.search);
        if (ACTION_ADD.equals(action)) {
            BookEditFields.EditMode editMode = BookEditFields.EditMode.valueOf(
                intent.getStringExtra(BookEditFields.EDIT_MODE));
            View fieldAuthor = findViewById(R.id.field_author);
            fieldAuthor.setVisibility(View.VISIBLE);
            EditText authorEdit = findViewById(R.id.edit_author);

            View fieldPublisher = findViewById(R.id.field_publisher);
            fieldPublisher.setVisibility(View.VISIBLE);
            EditText publisherEdit = findViewById(R.id.edit_publisher);

            View fieldPublishTime = findViewById(R.id.field_publish_time);
            fieldPublishTime.setVisibility(View.VISIBLE);
            EditText publishTimeEdit = findViewById(R.id.edit_publish_time);

            View fieldPriceEdit = findViewById(R.id.field_price);
            fieldPriceEdit.setVisibility(View.VISIBLE);
            EditText priceEdit = findViewById(R.id.edit_price);
            Calendar calendar = Calendar.getInstance();
            if (editMode == BookEditFields.EditMode.MODIFY) {
                setTitle(R.string.edit_book);
                bookNameEdit.setText(intent.getStringExtra(BookEditFields.BOOK_NAME));
                authorEdit.setText(intent.getStringExtra(BookEditFields.AUTHOR));
                priceEdit.setText(String.format(Locale.getDefault(), "%.2f",
                    intent.getDoubleExtra(BookEditFields.PRICE, Double.MIN_VALUE)));
                publisherEdit.setText(intent.getStringExtra(BookEditFields.PUBLISHER));
                String borrowTime = intent.getStringExtra(BookEditFields.PUBLISH_TIME);
                Date date = DateUtils.parseDate(borrowTime);
                calendar.setTime(date);
                publishTimeEdit.setText(borrowTime);
                addButton.setText(R.string.modify);
            }

            publishTimeEdit.setOnClickListener(
                v -> new DatePickerDialog(EditActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                    (view, year, month, dayOfMonth) -> {
                        // Month start from 0
                        String dateTime = year + "-" + (month + 1) + "-" + dayOfMonth;
                        calendar.set(year, month, dayOfMonth);
                        publishTimeEdit.setText(dateTime);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show());

            final int bookId = intent.getIntExtra(BookEditFields.BOOK_ID, -1);
            addButton.setOnClickListener(v -> {
                if ("".equals(bookNameEdit.getText().toString()) && "".equals(authorEdit.getText().toString())
                    && "".equals(publisherEdit.getText().toString())) {
                    onBackPressed();
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra(BookEditFields.BOOK_ID, bookId);
                resultIntent.putExtra(BookEditFields.BOOK_NAME, bookNameEdit.getText().toString());
                if (!"".equals(priceEdit.getText().toString())) {
                    resultIntent.putExtra(BookEditFields.PRICE, Double.parseDouble(priceEdit.getText().toString()));
                }
                resultIntent.putExtra(BookEditFields.AUTHOR, authorEdit.getText().toString());
                resultIntent.putExtra(BookEditFields.PUBLISHER, publisherEdit.getText().toString());
                resultIntent.putExtra(BookEditFields.PUBLISH_TIME, publishTimeEdit.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            });
            searchButton.setVisibility(View.GONE);
        } else if (ACTION_SEARCH.equals(action)) {
            setTitle(R.string.search_book);
            View fieldShowCount = findViewById(R.id.field_show_count);
            fieldShowCount.setVisibility(View.VISIBLE);
            EditText showCountEdit = findViewById(R.id.edit_show_count);

            View fieldSearchPriceView = findViewById(R.id.field_search_price);
            fieldSearchPriceView.setVisibility(View.VISIBLE);
            EditText lowestPriceEdit = findViewById(R.id.lowest_price);
            EditText highestPriceEdit = findViewById(R.id.highest_price);
            searchButton.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(BookEditFields.BOOK_NAME, bookNameEdit.getText().toString());
                if (!"".equals(lowestPriceEdit.getText().toString())) {
                    resultIntent.putExtra(BookEditFields.LOWEST_PRICE,
                        Double.parseDouble(lowestPriceEdit.getText().toString()));
                }
                if (!"".equals(highestPriceEdit.getText().toString())) {
                    resultIntent.putExtra(BookEditFields.HIGHEST_PRICE,
                        Double.parseDouble(highestPriceEdit.getText().toString()));
                }
                String showCount = showCountEdit.getText().toString();
                if (!showCount.isEmpty()) {
                    resultIntent.putExtra(BookEditFields.SHOW_COUNT, Integer.parseInt(showCount));
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            });
            addButton.setVisibility(View.GONE);
        } else {
            // Something wrong, just return
            finish();
            return;
        }

        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
