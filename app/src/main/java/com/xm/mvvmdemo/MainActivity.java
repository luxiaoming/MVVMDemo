package com.xm.mvvmdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xm.mvvmdemo.Component.MyComponent;
import com.xm.mvvmdemo.Model.User;
import com.xm.mvvmdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String str = "代码GG之家微信号\n" +
            "code_gg_home\n" +
            "欢迎关注！";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        final ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main,new MyComponent());


        final User user = new User();


        binding.setUser(user);
        user.setName(str);
        user.setAge(28);

        binding.age.post(new Runnable() {
            @Override
            public void run() {
                binding.age.setText("30");
                user.setAge(user.getAge());
            }
        });

    }

}
