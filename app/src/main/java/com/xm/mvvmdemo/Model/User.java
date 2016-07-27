package com.xm.mvvmdemo.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.xm.mvvmdemo.BR;

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
