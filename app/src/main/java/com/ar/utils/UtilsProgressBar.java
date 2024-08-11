package com.ar.utils;


import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class UtilsProgressBar {
    public static KProgressHUD showProgressDialog(Context context) {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }
}