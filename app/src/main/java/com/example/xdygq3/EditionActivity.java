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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EditionActivity extends AppCompatActivity {

    public Context context = this;
    public Classes.SettingsData config = null;
    public ConcurrentHashMap<Integer, Classes.Count> Counts = new ConcurrentHashMap<>();

    public Thread thread = new Thread(() -> {
        String data = Functions.getFile(context, "data.json");
        config = new Gson().fromJson(data, Classes.SettingsData.class);
        List<Classes.Post> Posts = config.Posts;
        runOnUiThread(() -> {
            TableLayout tableLayout = findViewById(R.id.TableLayout2);
            TableRow top = new TableRow(context);
            top.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableLayout.addView(top);
            Functions.addTextRow(context, top, new Classes.Compat[]{new Classes.Compat("串号"), new Classes.Compat("备注"), new Classes.Compat("只看Po")});
            for (int i = 0; i < Posts.size(); i++) {
                Classes.Post item = Posts.get(i);
                TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                Functions.addEditRow(context, tableRow, new Classes.Compat[]{new Classes.Compat(Integer.toString(item.Id), View.generateViewId(), "id"), new Classes.Compat(item.Mark, View.generateViewId(), "mark")});
                Counts.put(item.Id, new Classes.Count(item.ReplyCount, item.NewCount));
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                SwitchCompat mySwitch = new SwitchCompat(context);
                mySwitch.setLayoutParams(layoutParams);
                mySwitch.setPadding(10, 10, 10, 10);
                mySwitch.setChecked(item.OnlyPo);
                mySwitch.setTag("onlyPo");
                tableRow.addView(mySwitch);
                Button myButton = new Button(context);
                myButton.setText("删除");
                myButton.setTag(Integer.toString(item.Id));
                myButton.setLayoutParams(layoutParams);
                myButton.setPadding(10, 10, 10, 10);
                myButton.setOnClickListener(new View.OnClickListener() {
                    public final String flag = Integer.toString(item.Id);

                    @Override
                    public void onClick(View v) {
                        int index = -1;
                        int childCount = tableLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View child = tableLayout.getChildAt(i);
                            if (child instanceof TableRow) {
                                TableRow tableRow = (TableRow) child;
                                int rowChildCount = tableRow.getChildCount();
                                for (int j = 0; j < rowChildCount; j++) {
                                    View rowChild = tableRow.getChildAt(j);
                                    if (rowChild instanceof Button) {
                                        Button button = (Button) rowChild;
                                        String tag = String.valueOf(button.getTag());
                                        if (tag.equals(flag)) {
                                            index = i;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (index != -1) break;
                        }
                        if (index != -1) {
                            tableLayout.removeViewAt(index);
                        }
                    }
                });
                tableRow.addView(myButton);
                tableLayout.addView(tableRow);
            }
        });
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition);
        thread.start();
        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("编辑");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24));
        toolbar.setContentInsetStartWithNavigation(10);
        toolbar.setNavigationOnClickListener(v -> {
            save();
            setResult(RESULT_OK, null);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, Functions.menuIconWithText(AppCompatResources.getDrawable(this, R.drawable.add), "添加")).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 1, Functions.menuIconWithText(AppCompatResources.getDrawable(this, R.drawable.save), "保存")).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                TableLayout tableLayout = findViewById(R.id.TableLayout2);
                TableRow tableRow = new TableRow(context);
                Functions.addEditRow(context, tableRow, new Classes.Compat[]{new Classes.Compat("60000000", View.generateViewId(), "id"), new Classes.Compat("新串", View.generateViewId(), "mark")});
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                SwitchCompat mySwitch = new SwitchCompat(context);
                mySwitch.setLayoutParams(layoutParams);
                mySwitch.setPadding(10, 10, 10, 10);
                mySwitch.setChecked(false);
                mySwitch.setTag("onlyPo");
                tableRow.addView(mySwitch);
                Button myButton = new Button(context);
                myButton.setText("删除");
                int id = View.generateViewId();
                myButton.setTag(Integer.toString(id));
                myButton.setLayoutParams(layoutParams);
                myButton.setPadding(10, 10, 10, 10);
                myButton.setOnClickListener(new View.OnClickListener() {
                    public final String flag = Integer.toString(id);

                    @Override
                    public void onClick(View v) {
                        int index = -1;
                        int childCount = tableLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View child = tableLayout.getChildAt(i);
                            if (child instanceof TableRow) {
                                TableRow tableRow = (TableRow) child;
                                int rowChildCount = tableRow.getChildCount();
                                for (int j = 0; j < rowChildCount; j++) {
                                    View rowChild = tableRow.getChildAt(j);
                                    if (rowChild instanceof Button) {
                                        Button button = (Button) rowChild;
                                        String tag = String.valueOf(button.getTag());
                                        if (tag.equals(flag)) {
                                            index = i;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (index != -1) break;
                        }
                        if (index != -1) {
                            tableLayout.removeViewAt(index);
                        }
                    }
                });
                tableRow.addView(myButton);
                tableLayout.addView(tableRow);
                break;
            case 1:
                save();
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, null);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void save() {
        TableLayout tableLayout = findViewById(R.id.TableLayout2);
        int childCount = tableLayout.getChildCount();
        int cnt = 0;
        config.Posts = new ArrayList<Classes.Post>();
        for (int i = 0; i < childCount; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow tableRow = (TableRow) child;
                int rowChildCount = tableRow.getChildCount();
                boolean available = false;
                while (config.Posts.size() <= cnt)
                    config.Posts.add(new Classes.Post());
                for (int j = 0; j < rowChildCount; j++) {
                    View rowChild = tableRow.getChildAt(j);
                    if (rowChild instanceof EditText) {
                        EditText editText = (EditText) rowChild;
                        String tag = String.valueOf(editText.getTag());
                        if (tag.equals("id")) {
                            int id = Integer.parseInt(editText.getText().toString());
                            config.Posts.get(cnt).Id = id;
                            Classes.Count tmp = Counts.get(id);
                            config.Posts.get(cnt).ReplyCount = tmp != null ? tmp.ReplyCount : 0;
                            config.Posts.get(cnt).NewCount = tmp != null ? tmp.NewCount : 0;
                            available = true;
                        } else if (tag.equals("mark")) {
                            config.Posts.get(cnt).Mark = editText.getText().toString();
                            available = true;
                        }
                    } else if (rowChild instanceof SwitchCompat) {
                        SwitchCompat mySwitch = (SwitchCompat) rowChild;
                        String tag = String.valueOf(mySwitch.getTag());
                        if (tag.equals("onlyPo")) {
                            config.Posts.get(cnt).OnlyPo = mySwitch.isChecked();
                            available = true;
                        }
                    } /*else if (rowChild instanceof TextView) {
                        TextView textView = (TextView) rowChild;
                        String text = textView.getText().toString();
                    }*/
                }
                if (available) cnt++;
            }
        }
        String contentData = new Gson().toJson(config);
        Functions.PutFile(context, "data.json", contentData);
    }
}
