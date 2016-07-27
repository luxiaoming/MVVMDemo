package com.xm.mvvmdemo.adapter;

import android.databinding.InverseBindingListener;
import android.databinding.adapters.ListenerUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.xm.mvvmdemo.R;


public class OptionAdapter2  extends BaseAdapter{
    @Override
    public void setAge(TextView text, int age) {
        if (!text.getText().equals(age + ""))
            text.setText(age + "");
    }


    @Override
    public void onAgeChange(TextView text, final InverseBindingListener ageAttrChanged) {

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
}
