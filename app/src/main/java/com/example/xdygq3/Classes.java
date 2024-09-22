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
        public int DelayTime = 20000;
        public int InnerDelayTime = 1000;
        public int textSize = 15;
        public boolean submitToServer = false;
        public boolean popWarning = true;
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
        public String content;
        public int id;
        public String tag;
        public int textSize;
        public int inputType;

        public Compat(String _c) {
            content = _c;
            id = View.generateViewId();
            tag = "";
            textSize = shareData.config != null ? shareData.config.textSize : 15;
            inputType = -1;
        }
        public Compat(String _c, int _i) {
            content = _c;
            id = _i;
            tag = "";
            textSize = shareData.config != null ? shareData.config.textSize : 15;
            inputType = -1;
        }
        public Compat(String _c, int _i, String _t) {
            content = _c;
            id = _i;
            tag = _t;
            textSize = shareData.config != null ? shareData.config.textSize : 15;
            inputType = -1;
        }
        public Compat(String _c, int _i, int _s) {
            content = _c;
            id = _i;
            tag = "";
            textSize = _s;
            inputType = -1;
        }
        public Compat(String _c, int _i, String _t, int _s) {
            content = _c;
            id = _i;
            tag = _t;
            textSize = _s;
            inputType = -1;
        }
        public Compat(String _c, int _i, String _t, Integer _s, int _type) {
            content = _c;
            id = _i;
            tag = _t;
            if(_s != null) textSize = _s;
            else textSize = shareData.config != null ? shareData.config.textSize : 15;
            inputType = _type;
        }
    }
}
