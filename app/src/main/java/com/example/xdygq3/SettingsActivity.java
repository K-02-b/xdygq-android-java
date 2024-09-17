package com.example.xdygq3;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SettingsActivity extends AppCompatActivity {

    public Classes.SettingsData config = shareData.config;

    private RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setToolbar();
        List<MyAdapter2.tuple> data = new ArrayList<>();
        data.add(new MyAdapter2.tuple("批次请求间隔（毫秒）", Integer.toString(config.DelayTime), "DelayTime"));
        data.add(new MyAdapter2.tuple("单次请求间隔（毫秒）", Integer.toString(config.InnerDelayTime), "InnerDelayTime"));
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter2 adapter = new MyAdapter2(this, data);
        recyclerView.setAdapter(adapter);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitle("编辑");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24));
        toolbar.setContentInsetStartWithNavigation(10);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_OK, null);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, Functions.menuIconWithText(AppCompatResources.getDrawable(this, R.drawable.save), "保存")).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int count = ((LinearLayoutManager) layoutManager).getItemCount();
            for (int i = 0; i < count; i++) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                if (viewHolder != null) {
                    MyAdapter2.MyViewHolder holder = new MyAdapter2.MyViewHolder(viewHolder.itemView);
                    String tag = holder.editText.getTag().toString();
                    if (tag.equals("DelayTime")) config.DelayTime = Integer.parseInt(holder.editText.getText().toString());
                    else if (tag.equals("InnerDelayTime")) config.InnerDelayTime = Integer.parseInt(holder.editText.getText().toString());
                }
            }
        }
        shareData.config = config;
        String contentData = new Gson().toJson(config);
        Functions.PutFile(this, "data.json", contentData);
        hint("已保存");
    }

    protected void hint(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

}
