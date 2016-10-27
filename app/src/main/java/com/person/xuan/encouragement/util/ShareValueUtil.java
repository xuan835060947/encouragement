package com.person.xuan.encouragement.util;

import com.google.gson.Gson;

/**
 * Created by chenxiaoxuan1 on 15/12/14.
 */
public class ShareValueUtil {
    public static final String PERSON_FILE = "person-file";
    public static final String WIDGET_FILE = "widget-file";
    public static final String HISTORY_FILE = "history-file";
    public static final String PERSON_STRING = "person-string";
    public static final String WIDGET_STRING = "widget-string";
    public static final String HISTORY_STRING = "history-string";

    public static final String CONTROL_SERVICE_RECEIVER_FILTER = "com.person.xuan.encouragement.control";

    public static String EXTERNAL_STORAGEP_FILE_PATH = "com.xuan.encouragement/";

    public static final Gson GSON = new Gson();

    public static final String KEY_ACTION = "KEY-ACTION";
    public static final String KEY_ID = "ID";

    public static final int ACTION_USE_STAR = 1001;
    public static final int ACTION_FINISH_PLAN = 1002;
    public static final int ACTION_ADD_PLAN = 1003;
    public static final int ACTION_WATCH_PLAN = 1004;
}
