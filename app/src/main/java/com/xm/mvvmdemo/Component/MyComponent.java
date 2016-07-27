package com.xm.mvvmdemo.Component;

import com.xm.mvvmdemo.adapter.BaseAdapter;
import com.xm.mvvmdemo.adapter.OptionAdapter;

/**
 * Created by Administrator on 2016/7/27.
 */

public class MyComponent implements android.databinding.DataBindingComponent {

    OptionAdapter OptionAdapter=new OptionAdapter();

    @Override
    public BaseAdapter getBaseAdapter() {
        return OptionAdapter;
    }
}
