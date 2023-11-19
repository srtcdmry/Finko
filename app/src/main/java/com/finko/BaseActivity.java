package com.finko;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.finko.component.EditTextValidationResult;
import com.finko.component.EditTextValidator;
import com.finko.component.LottieProgress;
import com.finko.component.Toasty;
import com.finko.helper.Constants;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BaseActivity extends AppCompatActivity {

    @Inject
    FirebaseAuth mAuth;

    private LottieProgress progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String message, int toastType, boolean withIcon) {
        runOnUiThread(() -> {
            int duration = switch (toastType) {
                case 5, 6, 7, 8, 9 -> Toasty.LENGTH_SHORT;
                default -> Toasty.LENGTH_LONG;
            };

            switch (toastType) {
                case 0, 5 ->
                        Toasty.success(BaseActivity.this, message, duration, withIcon).show();
                case 1, 6 ->
                        Toasty.error(BaseActivity.this, message, duration, withIcon).show();
                case 2, 7 -> Toasty.info(BaseActivity.this, message, duration, withIcon).show();
                case 3, 8 ->
                        Toasty.warning(BaseActivity.this, message, duration, withIcon).show();
                default ->
                        Toasty.normal(BaseActivity.this, message, duration, AppCompatResources.getDrawable(BaseActivity.this, R.mipmap.ic_custom_toast_icon)).show();
            }
        });

    }

    public void showProgressDialog() {
        showProgressDialog(null);
    }

    public void showProgressDialog(String s) {
        runOnUiThread(() -> {
            if (progressDialog == null
                    || progressDialog.getOwnerActivity() == null
                    || progressDialog.getOwnerActivity().isDestroyed()
                    || progressDialog.getOwnerActivity().isFinishing()) {
                initProgressDialogInstance();
            }

            if (s != null && !s.trim().isEmpty()) {
                progressDialog.init(s);
            } else {
                progressDialog.init("Lütfen bekleyiniz..");
            }

            try {
                progressDialog.show();
            } catch (Exception e) {
               showToast("BadTokenException " + e.getMessage(), Constants.TOAST_ERROR, true);
            }
        });
    }

    public void initProgressDialogInstance() {
        if (progressDialog == null
                || progressDialog.getOwnerActivity() == null
                || progressDialog.getOwnerActivity().isDestroyed()
                || progressDialog.getOwnerActivity().isFinishing()) {
            progressDialog = new LottieProgress(this);
            progressDialog.setOwnerActivity(this);
            progressDialog.setCancelable(false);
        }
    }

    public void dismissProgressDialog() {
        runOnUiThread(() -> {
            if (progressDialog != null
                    && progressDialog.getOwnerActivity() != null
                    && !progressDialog.getOwnerActivity().isDestroyed()
                    && !progressDialog.getOwnerActivity().isFinishing()) {
                progressDialog.dismiss();
            }
        });
    }

    public void logout(){
        mAuth.signOut();
        SharedPreferencesUtil.clear(this);
    }

    public EditTextValidator mailWatcher(){
            EditTextValidator mailValidator = input -> {
                if (TextUtils.isEmpty(input)) {
                    return new EditTextValidationResult(false, "Mail adresi boş olamaz.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    return new EditTextValidationResult(false, "Mail adresinizi doğru formatta girin.");
                } else {
                    return new EditTextValidationResult(true, "");
                }
            };

            return mailValidator;
    }

    public EditTextValidator passwordWatcher(){
        EditTextValidator passwordValidator = input -> {
            if (TextUtils.isEmpty(input)) {
                return new EditTextValidationResult(false, "Şifre boş olamaz.");
            } else if (input.length() < 8) {
                return new EditTextValidationResult(false, "Şifre 8 karakterden kısa olamaz");
            } else {
                return new EditTextValidationResult(true, "");
            }
        };

        return passwordValidator;
    }


}
