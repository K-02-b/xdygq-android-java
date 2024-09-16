package com.example.xdygq3;

import android.view.View;

import java.util.List;

public class Classes {

    public static class Post {
        public int Id;
        public String Mark;
        public boolean OnlyPo;
        public int ReplyCount;
        public int NewCount;
    }

    public static class SettingsData {
        public String UserHash = "";
        public List<Post> Posts;
        public int DelayTime = 5000;
        public int InnerDelayTime = 1000;
    }

    public static class Count {
        public int ReplyCount;
        public int NewCount;
        public int latest;

        public Count(int _r, int _n) {
            ReplyCount = _r;
            NewCount = _n;
            latest = _n;
        }

        public Count(int _r, int _n, int _l) {
            ReplyCount = _r;
            NewCount = _n;
            latest = _l;
        }
    }

    public static class Compat {
        String content;
        int id;
        String tag;

        public Compat(String _c) {
            content = _c;
            id = View.generateViewId();
            tag = "";
        }

        public Compat(String _c, int _i) {
            content = _c;
            id = _i;
            tag = "";
        }

        public Compat(String _c, int _i, String _t) {
            content = _c;
            id = _i;
            tag = _t;
        }
    }
}
