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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huawei.agc.clouddb.quickstart.model.LoginHelper;
import com.huawei.agconnect.auth.SignInResult;

public class AboutMeFragment extends Fragment implements LoginHelper.OnLoginEventCallBack {

    private View mHintLoginView;

    private View mLoginUserInfoView;

    private TextView mUserNameView;

    private TextView mAccountNameView;

    private MainActivity mActivity;

    static Fragment newInstance() {
        return new AboutMeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.getLoginHelper().addLoginCallBack(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        initLoginView(rootView);
        initUserDetailView(rootView);
        return rootView;
    }

    private void initLoginView(View rootView) {
        mHintLoginView = rootView.findViewById(R.id.hint_login_card);
        mHintLoginView.setOnClickListener(v -> logIn());
    }

    private void initUserDetailView(View rootView) {
        mLoginUserInfoView = rootView.findViewById(R.id.login_user_info);
        View nickNameView = mLoginUserInfoView.findViewById(R.id.nick_name);
        TextView nickNameTitleView = mLoginUserInfoView.findViewById(R.id.title);
        nickNameTitleView.setText(R.string.nick_name);
        mUserNameView = nickNameView.findViewById(R.id.details);

        View accountView = mLoginUserInfoView.findViewById(R.id.account);
        TextView accountNameTitleView = accountView.findViewById(R.id.title);
        accountNameTitleView.setText(R.string.account);
        mAccountNameView = accountView.findViewById(R.id.details);

        View passwordView = mLoginUserInfoView.findViewById(R.id.password);
        TextView passwordTitleView = passwordView.findViewById(R.id.title);
        passwordTitleView.setText(R.string.password);
        TextView passwordDetailView = passwordView.findViewById(R.id.details);
        passwordDetailView.setText("*****");

        View logoutView = mLoginUserInfoView.findViewById(R.id.logout);
        logoutView.setOnClickListener(v -> logOut());
    }

    private void logIn() {
        mActivity.getLoginHelper().login();
    }

    private void logOut() {
        mActivity.getLoginHelper().logOut();
        onLogOut(false);
    }

    @Override
    public void onLogin(boolean showLoginUserInfo, SignInResult signInResult) {
        if (showLoginUserInfo) {
            mHintLoginView.setVisibility(View.GONE);
            mLoginUserInfoView.setVisibility(View.VISIBLE);
            mUserNameView.setText(signInResult.getUser().getDisplayName());
            mAccountNameView.setText(signInResult.getUser().getDisplayName());
        } else {
            mHintLoginView.setVisibility(View.VISIBLE);
            mLoginUserInfoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLogOut(boolean showLoginUserInfo) {
        if (showLoginUserInfo) {
            mHintLoginView.setVisibility(View.GONE);
            mLoginUserInfoView.setVisibility(View.VISIBLE);
        } else {
            mHintLoginView.setVisibility(View.VISIBLE);
            mLoginUserInfoView.setVisibility(View.GONE);
        }
    }
}
