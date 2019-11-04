package com.ncr.smartkart.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ncr.smartkart.R;

/**
 * Created by pinna on 12/29/2017.
 */

public class Utils {
    public static ProgressDialog showLoadingDialog(Context context, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.skyBlue)));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(cancelable);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;
    }

    public static void loadImage(String picUrl, final ImageView imageView, final Context context) {
        Glide.with(context)
                .load(picUrl)
                .asBitmap().dontAnimate().fitCenter().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                        .create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}