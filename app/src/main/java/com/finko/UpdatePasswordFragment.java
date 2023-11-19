package com.finko;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.finko.component.GenericTextWatcher;
import com.finko.databinding.FragmentUpdatePasswordBinding;
import com.finko.helper.Constants;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdatePasswordFragment extends BaseFragment implements View.OnClickListener {
    @Inject
    FirebaseAuth mAuth;
    FragmentUpdatePasswordBinding binding;
    boolean isResetPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordBinding.inflate(Objects.requireNonNull(inflater), container, false);
        binding.setListener(this);
        getToolbarBackButton().setOnClickListener(this);
        isResetPassword = requireArguments().getBoolean("isResetPassword");
        setToolbarText(isResetPassword ? "Reset Password" : "Set Password");
        setUi();
        fieldsTextWatcher();
        return binding.getRoot();
    }

    private void fieldsTextWatcher() {
        Objects.requireNonNull(binding.editEmail.getEditText()).addTextChangedListener(new GenericTextWatcher(binding.newPassword, mailWatcher()));
        Objects.requireNonNull(binding.newPassword.getEditText()).addTextChangedListener(new GenericTextWatcher(binding.newPassword, passwordWatcher()));
        Objects.requireNonNull(binding.confPassword.getEditText()).addTextChangedListener(new GenericTextWatcher(binding.confPassword, passwordWatcher()));
    }

    private void setUi() {
        requireActivity().runOnUiThread(() -> {
            if (isResetPassword) {
                binding.passMessage.setText("Şifrenizi yenilemek için lütfen mail adresinizi giriniz. Yenileme linki mail adresinize gönderilecektir.");
                binding.editEmail.setVisibility(View.VISIBLE);
                binding.buttonSetPassword.setVisibility(View.GONE);
                binding.newPassword.setVisibility(View.GONE);
                binding.confPassword.setVisibility(View.GONE);
                binding.buttonResetPassword.setVisibility(View.VISIBLE);
            } else {
                binding.passMessage.setText("Uygulamaya kaydınızı tamamlayabilmek için lütfen giriş şifrenizi belirleyiniz.");
            }

        });

    }

    @Override
    public void onClick(View v) {
        if (v == binding.buttonSetPassword) {
            setPassword();
        } else if (v == binding.buttonResetPassword) {
            resetPassword(binding.editEmail.getEditText().getText().toString());
        } else if (v == getToolbarBackButton()) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_passwordUpdateFragment_to_loginFragment);
        }
    }

    private void setPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = Objects.requireNonNull(binding.newPassword.getEditText()).getText().toString();

        if (user != null) {
            if (passwordCheck()) {
                showProgressDialog("Şifreniz oluşturuluyor..");
                user.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            dismissProgressDialog();
                            if (task.isSuccessful()) {
                                showToast("Şifreniz başarıyla oluşturuldu.", Constants.TOAST_SUCCESS, true);
                                goToHome();
                            } else {
                                showToast(task.getException().getLocalizedMessage(), Constants.TOAST_ERROR, true);
                            }
                        });
            } else {
                showToast("Lütfen bilgilerinizi kontrol ediniz.", Constants.TOAST_ERROR, true);
            }
        } else {
            showToast("Beklenmedik bir hata oluştu. Lütfen tekrar deneyiniz.", Constants.TOAST_ERROR, true);
        }
    }

    private void goToHome() {
        startActivity(new Intent(requireActivity(), HomeActivity.class));
        requireActivity().finish();
    }

    private void resetPassword(String email) {

        if (TextUtils.isEmpty(email)){
            showToast("Lütfen mail adresininizi girin.", Constants.TOAST_WARNING, true);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Lütfen mail adresinizi doğru formatta girin.", Constants.TOAST_WARNING, true);
        } else {
            showProgressDialog();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        dismissProgressDialog();
                        if (task.isSuccessful()) {
                            showToast("Şifre yenileme bağlantısı girmiş olduğunuz mail adresine gönderildi.", Constants.TOAST_SUCCESS, true);
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_passwordUpdateFragment_to_loginFragment);
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException e) {
                                showToast("Girdiğiniz mail adresine ait bir kullanıcı bulunamadı.", Constants.TOAST_ERROR, true);
                            } catch (FirebaseTooManyRequestsException e){
                                showToast("Çok fazla şifre yenileme isteği gönderdiniz. Lütfen daha sonra tekrar deneyiniz.", Constants.TOAST_ERROR, true);
                            } catch (Exception e){
                                System.out.println("HATA : " + e + e.getLocalizedMessage());
                                showToast("Beklenmedik bir hata oluştu. Lütfen tekrar deneyin", Constants.TOAST_ERROR, true);
                            }
                        }
                    });
        }
    }

    private boolean passwordCheck() {
        String newPassword = Objects.requireNonNull(binding.newPassword.getEditText()).getText().toString();
        String confirmPassword = Objects.requireNonNull(binding.confPassword.getEditText()).getText().toString();

        boolean checkNewAndConfirmPassword = !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(confirmPassword) && newPassword.equals(confirmPassword) && newPassword.length() >= 8 && confirmPassword.length() >= 8;

        return checkNewAndConfirmPassword;
    }
}