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

package com.huawei.agc.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;

public abstract class ThirdBaseActivity extends AppCompatActivity {
    protected AGConnectAuth auth;
    private boolean link;

    private Button loginBtn;
    private Button linkBtn;
    private Button unlinkBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_base);
        link = getIntent().getBooleanExtra("link", false);
        auth = AGConnectAuth.getInstance();

        loginBtn = findViewById(R.id.btn_third_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        linkBtn = findViewById(R.id.link);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link();
            }
        });

        unlinkBtn = findViewById(R.id.unlink);
        unlinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlink();
            }
        });

        if (link) {
            loginBtn.setVisibility(View.INVISIBLE);
        } else {
            linkBtn.setVisibility(View.INVISIBLE);
            unlinkBtn.setVisibility(View.INVISIBLE);
        }
    }

    public abstract void login();

    public abstract void link();

    public abstract void unlink();

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void loginSuccess() {
        startActivity(new Intent(this, AuthMainActivity.class));
        finish();
    }
}
