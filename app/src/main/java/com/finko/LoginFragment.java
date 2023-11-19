package com.finko;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.finko.component.GenericTextWatcher;
import com.finko.databinding.FragmentLoginBinding;
import com.finko.helper.Constants;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    @Inject
    FirebaseAuth mAuth;
    FragmentLoginBinding binding;
    private BeginSignInRequest signInRequest;
    private SignInClient oneTapClient;
    private static final int REQ_ONE_TAP = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setListener(this);
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        getToolbarBackButton().setOnClickListener(this);
        setToolbarText("Login");
        setUserInformation();

        fieldsTextWatcher();

        oneTapClient = Identity.getSignInClient(requireActivity());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.buttonLogin) {
            login(Objects.requireNonNull(binding.editEmail.getEditText()).getText().toString(), Objects.requireNonNull(binding.editPassword.getEditText()).getText().toString());
        } else if (v == binding.buttonGoogle) {
            googleSign();
        } else if (v == binding.buttonResetPassword) {
            resetPasswordAction();
        } else if (v == getToolbarBackButton()) {
            requireActivity().finish();
        } else if (v == binding.buttonContinue) {
            passwordCheckEmailAndHasPassword(binding.editEmail.getEditText().getText().toString());
        } else if (v == binding.deleteMail) {
            deleteMailAction();
        }
    }

    private void fieldsTextWatcher() {
        Objects.requireNonNull(binding.editEmail.getEditText()).addTextChangedListener(new GenericTextWatcher(binding.editEmail, mailWatcher()));
        Objects.requireNonNull(binding.editPassword.getEditText()).addTextChangedListener(new GenericTextWatcher(binding.editPassword, passwordWatcher()));
    }

        private void resetPasswordAction() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isResetPassword", true);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_updatePasswordFragment, bundle);
    }

    private void deleteMailAction() {
        binding.editEmail.setEnabled(true);
        binding.deleteMail.setVisibility(View.GONE);
        binding.editPassword.setVisibility(View.GONE);
        binding.buttonContinue.setVisibility(View.VISIBLE);
        binding.buttonLogin.setVisibility(View.GONE);
    }

    private void setUserInformation() {
        String userMail = SharedPreferencesUtil.userMail(requireActivity());
        if (userMail != null) {
            Objects.requireNonNull(binding.editEmail.getEditText()).setText(userMail);
        }
    }

    private void passwordCheckEmailAndHasPassword(String email) {
        if (TextUtils.isEmpty(email)) {
            showToast("Lütfen mail adresininizi girin.", Constants.TOAST_WARNING, true);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Lütfen mail adresinizi doğru formatta girin.", Constants.TOAST_WARNING, true);
        } else {
            showProgressDialog("Email adresiniz doğrulanıyor..");
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                List<String> signInMethods = task.getResult().getSignInMethods();
                dismissProgressDialog();
                if (task.isSuccessful()) {
                    System.out.println(signInMethods.toString());
                    if ((signInMethods == null || signInMethods.isEmpty())) {
                        showToast("Hesap bulunamadı. Hesabınız yoksa lütfen google ile oturum açın.", Constants.TOAST_ERROR, true);
                    } else if (!signInMethods.contains("password")) {
                        showToast("Şifreniz henüz oluşturulmamış. Lütfen google ile oturum açarak veya reset password adımını takip ederek şifrenizi oluşturun.", Constants.TOAST_ERROR, true);
                    } else {
                        showToast("Şifreniz ile giriş yapabilirsiniz.", Constants.TOAST_SUCCESS, true);
                        binding.editEmail.setEnabled(false);
                        binding.deleteMail.setVisibility(View.VISIBLE);
                        binding.editPassword.setVisibility(View.VISIBLE);
                        binding.buttonContinue.setVisibility(View.GONE);
                        binding.buttonLogin.setVisibility(View.VISIBLE);
                    }
                } else {
                    showToast(task.getException().getLocalizedMessage(), Constants.TOAST_ERROR, true);
                }
            });
        }

    }

    private void login(String email, String password) {
        if (TextUtils.isEmpty(password)) {
            showToast("Lütfen şifrenizi girin.", Constants.TOAST_WARNING, true);
        } else if (password.length() < 8) {
            showToast("Şifreniz en az 8 karakter uzunluğunda olmalıdır.", Constants.TOAST_WARNING, true);
        } else {
            showProgressDialog("Giriş yapılıyor..");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        dismissProgressDialog();
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferencesUtil.setPrefUserMail(requireContext(), email);
                            goToHome();
                            showToast("Giriş başarılı.", Constants.TOAST_SUCCESS, true);
                        } else {
                            try {
                                Log.d("TAG", String.valueOf(task.getException()));
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException e) {
                                String errorMessage = "Kullanıcı bulunamadı. Lütfen e-posta adresinizi kontrol edin.";
                                showToast(errorMessage, Constants.TOAST_ERROR, true);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                String errorMessage = "Geçersiz kimlik bilgileri. Lütfen şifrenizi kontrol edin.";
                                showToast(errorMessage, Constants.TOAST_ERROR, true);
                            } catch (Exception e) {
                                // Diğer hatalar
                                String errorMessage = "Giriş yaparken bir hata oluştu.";
                                showToast(errorMessage, Constants.TOAST_ERROR, true);
                            }
                        }
                    });
        }
    }

    private void googleSign() {
        showProgressDialog();
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0, null);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("TAG", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dismissProgressDialog();
                        showToast(e.getLocalizedMessage(), Constants.TOAST_ERROR, true);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = googleCredential.getGoogleIdToken();
                    if (idToken != null) {
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.fetchSignInMethodsForEmail(googleCredential.getId()).addOnCompleteListener(task -> {
                            List<String> signInMethods = task.getResult().getSignInMethods();
                            if (task.isSuccessful()) {
                                if ((signInMethods == null || signInMethods.isEmpty()) || !signInMethods.contains("google.com") || !signInMethods.contains("password")) {
                                    mAuth.signInWithCredential(firebaseCredential)
                                            .addOnCompleteListener(requireActivity(), resultTask -> {
                                                dismissProgressDialog();
                                                if (resultTask.isSuccessful()) {
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    SharedPreferencesUtil.setPrefUserMail(requireActivity(), Objects.requireNonNull(user).getEmail());
                                                    if (!signInMethods.contains("password")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putBoolean("isResetPassword", false);
                                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_updatePasswordFragment, bundle);
                                                    } else {
                                                        goToHome();
                                                    }
                                                } else {
                                                    showToast(Objects.requireNonNull(resultTask.getException()).getLocalizedMessage(), Constants.TOAST_ERROR, true);
                                                }
                                            });
                                } else {
                                    SharedPreferencesUtil.setPrefUserMail(requireContext(), googleCredential.getId());
                                    goToHome();
                                }
                            } else {
                                dismissProgressDialog();
                                showToast(task.getException().getLocalizedMessage(), Constants.TOAST_ERROR, true);
                            }

                        });


                    }

                } catch (ApiException e) {
                    // ...
                }
                break;
        }
    }

    private void goToHome() {
        startActivity(new Intent(requireActivity(), HomeActivity.class));
        requireActivity().finish();
    }
}