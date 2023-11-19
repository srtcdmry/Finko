package com.finko.component;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.WindowManager;

import com.finko.databinding.LottieProgressLayoutBinding;

public class LottieProgress extends Dialog {
    private final LottieProgressLayoutBinding binding;

    public LottieProgress(Context context) {
        super(context, com.finko.R.style.lottieDialogTheme);
        binding = LottieProgressLayoutBinding.inflate(getLayoutInflater());

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        getWindow().setLayout((MATCH_PARENT), (MATCH_PARENT));
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(binding.getRoot());

        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);


    }

    public void init(String message) {
        if (message != null) {
            binding.progressD.setText(message);
        }
    }
}
