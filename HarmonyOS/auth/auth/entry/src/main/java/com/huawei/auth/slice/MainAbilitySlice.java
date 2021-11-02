package com.huawei.auth.slice;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.auth.ResourceTable;
import com.huawei.hmf.tasks.HarmonyTask;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.TaskExecutors;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.TextAlignment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {
    private final static HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x0001,"auth_demo");


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button verify = (Button) findComponentById(ResourceTable.Id_Verify);
        verify.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                verify();
            }
        });

        Button login = (Button) findComponentById(ResourceTable.Id_Login);
        login.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                login();
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void verify() {
        String countryCode = getCountryCode();
        String phoneNumber = getPhoneNumber();

        int action = VerifyCodeSettings.ACTION_REGISTER_LOGIN;
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                .action(action)
                .sendInterval(30)
                .build();

        HarmonyTask<VerifyCodeResult> task = AGConnectAuth.getInstance()
                .requestVerifyCode(countryCode, phoneNumber, settings);

        task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
            @Override
            public void onSuccess(VerifyCodeResult verifyCodeResult) {
                log("verifyPhoneCode success");
            }
        }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                log("verifyPhoneCode fail:" + e);
            }
        });

    }

    private void login() {
        String countryCode = getCountryCode();
        String phoneNumber = getPhoneNumber();
        String password = getPassword();
        String verifyCode = getVerify();

        /* create a phone credential
         * the credential used for login.
         * password and verifyCode can not be both null.
         */
        AGConnectAuthCredential phoneAuthCredential =
                PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode);

        /* signIn */
        HarmonyTask<SignInResult> task = AGConnectAuth.getInstance().signIn(phoneAuthCredential);
        task.addOnSuccessListener(new OnSuccessListener<SignInResult>() {
            @Override
            public void onSuccess(SignInResult signInResult) {
                log("signIn success");
                showUser(signInResult.getUser());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                log("signInWithCode fail:" + e);
            }
        });
    }

    private String getCountryCode() {
        TextField field = (TextField) findComponentById(ResourceTable.Id_countryCode);
        return field.getText();
    }

    private String getPhoneNumber() {
        TextField field = (TextField) findComponentById(ResourceTable.Id_phoneNumber);
        return field.getText();
    }

    private String getPassword() {
        TextField field = (TextField) findComponentById(ResourceTable.Id_password);
        return field.getText();
    }

    private String getVerify() {
        TextField field = (TextField) findComponentById(ResourceTable.Id_verifyCode);
        return field.getText();
    }

    private void showUser(AGConnectUser user) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n" + "Anonymous:" + user.isAnonymous());
        buffer.append("\n" + "Uid:" + user.getUid());
        buffer.append("\n" + "DisplayName:" + user.getDisplayName());
        buffer.append("\n" + "PhotoUrl:" + user.getPhotoUrl());
        buffer.append("\n" + "Email:" + user.getEmail());
        buffer.append("\n" + "Phone:" + user.getPhone());
        buffer.append("\n" + "ProviderId:" + user.getProviderId());
        buffer.append("\n" + "EmailVerified:" + user.getEmailVerified());
        buffer.append("\n" + "PasswordSetted:" + user.getPasswordSetted());

        DirectionalLayout directionalLayout = new DirectionalLayout(getContext());
        directionalLayout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        directionalLayout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);
        directionalLayout.setOrientation(Component.VERTICAL);
        directionalLayout.setPadding(16, 16, 16, 16);

        Component logView = getLogView(buffer);
        directionalLayout.addComponent(logView);
        setUIContent(directionalLayout);
    }

    private void log(String format, Object... args) {
        HiLog.debug(LABEL,format, args);
    }

    private Component getLogView(StringBuffer buffer) {
        Text text = new Text(getContext());
        text.setMultipleLine(true);
        text.setTextSize(30);
        text.setText(buffer.toString());
        text.setTextAlignment(TextAlignment.START);
        ComponentContainer.LayoutConfig textLayoutConfig = new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        text.setLayoutConfig(textLayoutConfig);
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addComponent(text);
        DirectionalLayout.LayoutConfig scrollLayoutConfig = new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        scrollLayoutConfig.weight = 1;
        scrollView.setLayoutConfig(scrollLayoutConfig);
        return scrollView;
    }
}

