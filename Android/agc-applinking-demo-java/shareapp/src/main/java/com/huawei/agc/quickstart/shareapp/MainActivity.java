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

package com.huawei.agc.quickstart.shareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));

        findViewById(R.id.button).setOnClickListener(v -> {
            String text = textView.getText().toString();
            if (text.length() > 0) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(text));
                startActivity(intent);
            }
        });

        EditText editText = findViewById(R.id.editText);
        findViewById(R.id.button2).setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (text.length() > 0) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(text));
                startActivity(intent);
            }
        });
    }
}
