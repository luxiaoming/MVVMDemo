package com.xm.mvvmdemo.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.ListenerUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.xm.mvvmdemo.BR;
import com.xm.mvvmdemo.R;

public class User extends BaseObservable {

    @Bindable
    private String name;

    @Bindable
    private int age;

    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    @BindingAdapter("age")
    public static void setAge(TextView text, int age) {
      if(!text.getText().equals(age+""))
        text.setText(age + "");
    }

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
    public static void onAgeChange(TextView text, final InverseBindingListener ageAttrChanged) {

        final TextWatcher newValue;
        newValue = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (ageAttrChanged != null) {
                    ageAttrChanged.onChange();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        final TextWatcher oldValue = ListenerUtil.trackListener(text, newValue, R.id.age);
        if (oldValue != null) {
            text.removeTextChangedListener(oldValue);
        }
        if (newValue != null) {
            text.addTextChangedListener(newValue);
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {

            this.age = age;
            notifyPropertyChanged(BR.age);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
