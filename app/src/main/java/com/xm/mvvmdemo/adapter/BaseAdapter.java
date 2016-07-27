package com.xm.mvvmdemo.adapter;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/27.
 */

public abstract class BaseAdapter {

    @BindingAdapter("age")
    public abstract void setAge(TextView text, int age) ;

    @InverseBindingAdapter(attribute = "age", event = "ageAttrChanged")
    public static int getAge(TextView view) {
        int age = -1;
        try {
            age = Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException ex) {
            age = -1;
        }

        return age;
    }
    @BindingAdapter("ageAttrChanged")
    public abstract void onAgeChange(TextView text, final InverseBindingListener ageAttrChanged) ;
}



