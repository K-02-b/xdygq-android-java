package com.example.xdygq3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public Context context = this;
    public Classes.SettingsData config = null;
    public ConcurrentHashMap<Integer, Classes.Count> Counts = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer, IdPair> Ids = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer, Boolean> published = new ConcurrentHashMap<>();

    public String DATAFILE = "data.json";
    public Thread thread = null;
    public NotificationManager notificationManager = null;
    public Runnable task = () -> {
        try {
            String data = Functions.getFile(context, DATAFILE);
            config = new Gson().fromJson(data, Classes.SettingsData.class);
        } catch (Throwable e) {
            config = new Classes.SettingsData();
            config.Posts = new ArrayList<>();
            String data = new Gson().toJson(config);
            Functions.PutFile(context, DATAFILE, data);
        }
        List<Classes.Post> Posts = config.Posts;
        for (int i = 0; i < Posts.size(); i++) {
            Classes.Post item = Posts.get(i);
            Counts.put(item.Id, new Classes.Count(item.ReplyCount, item.NewCount));
        }
        runOnUiThread(() -> {
            TableLayout tableLayout = findViewById(R.id.TableLayout);
            while (tableLayout.getChildCount() > 0) {
                tableLayout.removeViewAt(0);
            }
            TableRow top = new TableRow(context);
            top.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableLayout.addView(top);
            Functions.addTextRow(context, top, new Classes.Compat[]{new Classes.Compat("串号"), new Classes.Compat("备注"), new Classes.Compat("回复"), new Classes.Compat("新")});
            for (int i = 0; i < Posts.size(); i++) {
                Classes.Post item = Posts.get(i);
                TableRow tableRow = new TableRow(context);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                int[] uniqueId = new int[]{View.generateViewId(), View.generateViewId()};
                Ids.put(item.Id, new IdPair(uniqueId[0], uniqueId[1]));
                Functions.addTextRow(context, tableRow, new Classes.Compat[]{new Classes.Compat(Integer.toString(item.Id)), new Classes.Compat(item.Mark), new Classes.Compat(Integer.toString(item.ReplyCount), uniqueId[0]), new Classes.Compat(Integer.toString(item.NewCount), uniqueId[1]), new Classes.Compat(item.OnlyPo ? "只看Po" : "")});
                Button button = new Button(this);
                button.setText("已读");
                button.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                button.setOnClickListener(v -> hasRead(item.Id, uniqueId[1]));
                tableRow.addView(button);
                tableLayout.addView(tableRow);
            }
        });
        Context appContext = getApplicationContext();
        JobScheduler scheduler = (JobScheduler) appContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(appContext, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(337845818, componentName)
                .setMinimumLatency(config.DelayTime)
                .setBackoffCriteria(config.DelayTime, JobInfo.BACKOFF_POLICY_LINEAR)
                .build();
        scheduler.schedule(jobInfo);
    };

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String param = intent.getStringExtra("id");
        int id = Integer.parseInt(Objects.requireNonNull(param));
        Counts.compute(id, (k, count) -> new Classes.Count(count != null ? count.ReplyCount : 0, 0));
        if (Boolean.TRUE.equals(published.get(id)))
            notificationManager.cancel(id);
        TextView newReplyTextView = findViewById(Objects.requireNonNull(Ids.get(id)).second);
        runOnUiThread(() -> newReplyTextView.setText("0"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        File path = getFilesDir();
        File file = new File(path, DATAFILE);
        if (!file.exists()) {
            try {
                FileOutputStream fos = openFileOutput(DATAFILE, Context.MODE_PRIVATE);
                config = new Classes.SettingsData();
                config.Posts = new ArrayList<>();
                String data = new Gson().toJson(config);
                fos.write(data.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                requestNotificationPermission();
            }
        }
        EventBus.getDefault().register(this);
        thread = new Thread(task);
        thread.start();
    }

    private static final int REQUEST_CODE_NOTIFICATIONS = 1;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, "your_notification_channel_id");
        startActivityForResult(intent, REQUEST_CODE_NOTIFICATIONS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 337845818) {
            if (resultCode == RESULT_OK) {
                if (thread.isAlive())
                    thread.interrupt();
                thread = new Thread(task);
                thread.start();
            }
        } else if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                    Toast.makeText(this, "未授予通知权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void sendNotification(Context context, int id, String title, String content) {
        if (notificationManager == null)
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "新消息通知";
            String description = "当有新消息时通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setAction("NOTIFICATION_CLICKED"); // 设置Action以便于receiver识别
        resultIntent.putExtra("id", Integer.toString(id));
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, id, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .build();
        notificationManager.notify(id, notification);
    }

    protected void update() throws InterruptedException {
        int DelayTime = config.DelayTime;
        int InnerDelayTime = config.InnerDelayTime;
        String UserHash = config.UserHash;
        new Thread(() -> {
            try {
                List<Classes.Post> Posts = config.Posts;
                for (Classes.Post item : Posts) {
                    String api = "https://api.nmb.best/api/thread/page/337845818/id/";
                    if (item.OnlyPo) api = "https://api.nmb.best/api/po/page/337845818/id/";
                    int id = item.Id;
                    String url = api + id;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .header("Cookie", "userhash=" + UserHash)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        private final int _id = item.Id;

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String responseBody = response.body().string();
                            try {
                                int newReplyCount = new Gson().fromJson(responseBody, JsonObject.class).getAsJsonObject().get("ReplyCount").getAsInt();
                                Classes.Count tmp = Counts.get(_id);
                                tmp = new Classes.Count(newReplyCount == 0 ? tmp != null ? tmp.ReplyCount : 0 : newReplyCount, (tmp != null ? tmp.NewCount : 0) + newReplyCount - (tmp != null ? tmp.ReplyCount : 0), tmp != null ? tmp.latest : 0);
                                boolean should = tmp.NewCount != tmp.latest;
                                tmp.latest = tmp.NewCount;
                                Counts.put(_id, tmp);
                                Classes.Count finalTmp = tmp;
                                runOnUiThread(() -> {
                                    IdPair IP = Ids.get(_id);
                                    TextView newReplyTextView = findViewById(IP != null ? IP.first : 0);
                                    newReplyTextView.setText(Integer.toString(finalTmp.ReplyCount));
                                    if (should) {
                                        sendNotification(context, item.Id, item.Mark, "共有" + finalTmp.NewCount + "条新消息");
                                        TextView newCountTextView = findViewById(IP != null ? IP.second : 0);
                                        newCountTextView.setText(Integer.toString(finalTmp.NewCount));
                                        published.put(item.Id, true);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        Thread.sleep(InnerDelayTime);
                    } catch (InterruptedException e) {
                        throw e;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(DelayTime);
        saveCount();
    }

    protected void hint(String message) {
        runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    protected void hint(int number) {
        runOnUiThread(() -> Toast.makeText(context, Integer.toString(number), Toast.LENGTH_SHORT).show());
    }

    protected void hasRead(int PostId, int CompatId) {
        runOnUiThread(() -> {
            TextView newReplyTextView = findViewById(CompatId);
            newReplyTextView.setText("0");
            if (Boolean.TRUE.equals(published.get(PostId)))
                notificationManager.cancel(PostId);
        });
        Counts.compute(PostId, (k, count) -> new Classes.Count(count != null ? count.ReplyCount : 0, 0));
        saveCount();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("养鸽器");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "编辑");
        menu.add(0, 1, 1, "设置");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                if (thread.isAlive())
                    thread.interrupt();
                saveCount();
                Intent editIntent = new Intent(this, EditionActivity.class);
                startActivityForResult(editIntent, 337845818);
                break;
            case 1:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (thread.isAlive())
            thread.interrupt();
        saveCount();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(EventMessage message) {
        try {
            update();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCount() {
        for (Classes.Post post : config.Posts) {
            Classes.Count tmp = Counts.get(post.Id);
            post.ReplyCount = tmp != null ? tmp.ReplyCount : 0;
            post.NewCount = tmp != null ? tmp.NewCount : 0;
        }
        String contentData = new Gson().toJson(config);
        Functions.PutFile(context, "data.json", contentData);
    }

    public static class IdPair {
        public int first;
        public int second;

        public IdPair(int _f, int _s) {
            first = _f;
            second = _s;
        }
    }

}