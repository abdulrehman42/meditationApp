package com.ar.utils

import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

object Utilsprogressbar {
    fun showProgressDialog(context: Context?): KProgressHUD {
        return KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2) //                .setBackgroundColor(Color.WHITE)
            //                .setWindowColor(Color.parseColor("#C4C4C6"))
            //                .setDetailsLabel("", Color.GRAY)
            .setDimAmount(0.5f)
    }
}