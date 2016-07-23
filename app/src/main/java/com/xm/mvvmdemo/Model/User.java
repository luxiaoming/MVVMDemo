package com.xm.mvvmdemo.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.xm.mvvmdemo.BR;

public class User extends BaseObservable{

    @Bindable
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
}
