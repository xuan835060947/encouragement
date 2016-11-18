package com.person.xuan.encouragement.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.person.xuan.encouragement.entity.History;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.file.FileTool;
import com.person.xuan.encouragement.util.ShareValueUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class EncouragementWrapper {
    private static Person mPerson;
    private static int mTextSize;

    public static void addHistoryPlan(Plan plan) {
        FileTool.addLine(ShareValueUtil.HISTORY_FILE, ShareValueUtil.GSON.toJson(plan));
    }

    //// TODO: 16/11/18  
    public static void getHistory(final OnGetHistory onGetHistory) {
        getHistoryFromFile(new OnGetHistoryFromFile() {
            @Override
            public void onGet(History history) {
                onGetHistory.onGet(history);
            }
        });
    }

    public static void getHistory(int startIndex,int endIndex,final OnGetHistory onGetHistory) {
        getHistoryFromFile(new OnGetHistoryFromFile() {
            @Override
            public void onGet(History history) {
                onGetHistory.onGet(history);
            }
        });
    }

    private static void getHistoryFromFile(final OnGetHistoryFromFile onGetHistory) {
        FileTool.getLines(ShareValueUtil.HISTORY_FILE, new FileTool.OnGetLine() {
            History history = new History();

            @Override
            public void onGetLine(String line, int lineNum) {
                if (lineNum >= 0) {
                    Plan plan = ShareValueUtil.GSON.fromJson(line, Plan.class);
                    if (plan != null) {
                        history.addPlan(plan);
                    }
                } else {
                    onGetHistory.onGet(history);
                }
            }
        });
    }

    public static void writeData(Context context, String fileName, String key, Object data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        String content = ShareValueUtil.GSON.toJson(data);
        editor.putString(key, content);
        editor.apply();
        FileTool.saveFile(fileName, content);
    }

    public static String getData(Context context, String fileName, String key) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String data = preferences.getString(key, null);
        return data;
    }

    public static void writePerson(Context context, Person person) {
        SharedPreferences.Editor editor = context.getSharedPreferences(ShareValueUtil.PERSON_FILE, Context.MODE_PRIVATE).edit();
        String content = ShareValueUtil.GSON.toJson(person);
        editor.putString(ShareValueUtil.PERSON_STRING, content);
        editor.apply();
    }

    public static Person getPerson(Context context) {
        if(mPerson != null){
            return mPerson;
        }
        SharedPreferences preferences = context.getSharedPreferences(ShareValueUtil.PERSON_FILE, Context.MODE_PRIVATE);
        String personStr = preferences.getString(ShareValueUtil.PERSON_STRING, null);
        if (personStr != null) {
            return ShareValueUtil.GSON.fromJson(personStr, Person.class);
        } else {
            return new Person();
        }
    }

    public static void writeWidgetId(Context context, Set<Integer> widgetIds) {
        Log.e("writeWidgetId", "start");
        SharedPreferences.Editor editor = context.getSharedPreferences(ShareValueUtil.WIDGET_FILE, Context.MODE_PRIVATE).edit();
        editor.putString(ShareValueUtil.WIDGET_STRING, ShareValueUtil.GSON.toJson(widgetIds));
        editor.apply();
        Log.e("writeWidgetId", "end");

    }

    public static Set<Integer> getWidgetIds(Context context) {
        Log.e("getWidgetIds", "start");
        SharedPreferences preferences = context.getSharedPreferences(ShareValueUtil.WIDGET_FILE, Context.MODE_PRIVATE);
        String str = preferences.getString(ShareValueUtil.WIDGET_STRING, null);
        Log.e("getWidgetIds", "preferences : " + str);
        Set<Integer> widgetIds = new HashSet<>();
        if (str != null) {
            Log.e("getWidgetIds", " " + widgetIds.getClass().getGenericSuperclass());
            widgetIds = ShareValueUtil.GSON.fromJson(str, new TypeToken<Set<Integer>>() {
            }.getType());
        } else {
            widgetIds = new HashSet<>();
        }
        Log.e("getWidgetIds", "end");
        return widgetIds;
    }

//    public static int getTextSize(Context context) {
//        if(mTextSize <= 0){
//            return mTextSize;
//        }
//
//    }

    public interface OnGetHistory {
        void onGet(History history);
    }

    private interface OnGetHistoryFromFile {
        void onGet(History history);
    }

}
