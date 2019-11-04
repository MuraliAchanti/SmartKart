package com.ncr.smartkart.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

public class BindingUtils {

    @BindingAdapter("android:text")
    public static void setFloat(TextView textView, Float f) {
        textView.setText(String.valueOf(f));
    }

    @BindingAdapter("android:text")
    public static void setFloat(TextView textView, Integer f) {
        textView.setText(String.valueOf(f));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Integer getInteger(TextView textView) {
        System.out.println(textView.getText().toString());
        return Integer.getInteger(textView.getText().toString());
    }
}
