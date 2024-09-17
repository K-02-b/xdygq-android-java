package com.example.xdygq3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final int ACTION_HOME = 1;
    private static final int ACTION_ABOUT = 2;

    RecyclerView recyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTitle("养鸽器");
        toolbar.setTitleTextColor(Color.WHITE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> items = new ArrayList<>();
        items.add("关于本软件：");
        items.add("- 软件名称：养鸽器");
        items.add("- 版本信息：内测版本");
        items.add("- 联系方式：如在使用过程中遇到任何问题或有任何建议，请通过以下方式联系我们：");
        items.add("    - 官方邮件：Kainas@foxmail.com");
        items.add("- 反馈与支持：我们非常重视您的反馈，并致力于不断改进我们的产品。如果您发现了BUG或是有宝贵的意见，欢迎您随时告知我们。");
        items.add("- 更新日志：本次更新修复了已知的问题，并对用户体验进行了优化。");
        items.add("- 感谢：感谢您选择使用我们的软件，我们将持续努力为您带来更好的服务体验。");
        adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottomView_about);
        bottomNavigationView.getMenu().findItem(R.id.navigation_item2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int action = getActionFromItemId(item.getItemId());
            switch (action) {
                case ACTION_HOME:
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return false;
                case ACTION_ABOUT:
                    return true;
                default:
                    return false;
            }
        });
    }

    private int getActionFromItemId(int itemId) {
        if (itemId == R.id.navigation_item1) return ACTION_HOME;
        else if (itemId == R.id.navigation_item2) return ACTION_ABOUT;
        return 0;
    }
}
