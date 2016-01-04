package com.person.xuan.encouragement.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.activity.HomeActivity;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.util.ShareValueUtil;
import com.person.xuan.encouragement.widget.EncouragementWrapper;

import java.util.Set;

/**
 * Created by chenxiaoxuan1 on 16/1/4.
 */
public class ControlService extends Service {
    private String TAG = "EncouragementWidgetProvider";
    private static final int ACTION_USE_STAR = 1001;
    private static final int ACTION_FINISH_PLAN = 1002;
    private static final int ACTION_ADD_PLAN = 1003;
    private static final String ACTION = "ACTION";
    private static final String POSITION = "POSITION";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter(ShareValueUtil.CONTROL_SERVICE_RECEIVER_FILTER);
        registerReceiver(controlReceiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(controlReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "controlReceiver onReceive");
            Person person = EncouragementWrapper.getPerson(context);
            int action = intent.getIntExtra(ACTION, -1);
            int position = intent.getIntExtra(POSITION, -1);
            Log.e(TAG, "onReceive " + "action : " + action + " position : " + position + " plan size : " + person.getPlans().size());
            if (action == ACTION_USE_STAR) {
                if (person.usedOneEncouragements()) {
                    Toast.makeText(context, "使用一颗星", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "星星不足,快完成点计划吧!", Toast.LENGTH_SHORT).show();
                }
            } else if (action == ACTION_FINISH_PLAN) {
                Log.e(TAG, "finish  ");
                EncouragementWrapper.addHistoryPlan(person.getPlans().get(position));
                person.finish(position);
            }
            EncouragementWrapper.writePerson(context, person);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            refreshView(context, EncouragementWrapper.getWidgetIds(context), appWidgetManager, person);
            Intent update = new Intent();
            update.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            sendBroadcast(update);
        }
    };

    private PendingIntent getStartActivityIntent(Context context, int action, int position, int flag) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        //flags 最好完全不同,并且不能使用负数
        PendingIntent pendingIntent = PendingIntent.getActivity(context, action, intent, flag);
        return pendingIntent;
    }

    private PendingIntent getPendingIntent(Context context, int action, int position, int flag) {
        Intent intent = new Intent();
        intent.setAction(ShareValueUtil.CONTROL_SERVICE_RECEIVER_FILTER);
        intent.putExtra(ACTION, action);
        if (action == ACTION_FINISH_PLAN) {
            intent.putExtra(POSITION, position);
        }
        Log.e(TAG, "getPendingIntent  action : " + action + "  position : " + position);
        //flags 最好完全不同,并且不能使用负数
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, action, intent, flag);
        return pendingIntent;
    }

    private void refreshView(Context context, Set<Integer> allWidgetId, AppWidgetManager appWidgetManager, Person person) {
        Log.e(TAG, "refresh View ");
        // 更新 widget
        for (Integer id : allWidgetId) {
            refreshWidget(context, id, appWidgetManager, person);
        }
    }

    private void refreshWidget(Context context, Integer id, AppWidgetManager appWidgetManager, Person person) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_show_window);
        widgetView.removeAllViews(R.id.ll_all_plan);
        int flags = 0;
        RemoteViews itemViews;
        for (int position = 0; position < person.getPlans().size(); position++) {
            Plan plan = person.getPlans().get(position);
            if (plan.getEncouragement() == 0) {
                itemViews = new RemoteViews(context.getPackageName(), R.layout.item_plan);
            } else {
                itemViews = new RemoteViews(context.getPackageName(), R.layout.item_reward_plan);
            }
            // 设置点击按钮对应的PendingIntent：即点击按钮时，发送广播。
            itemViews.setOnClickPendingIntent(R.id.iv_finish, getPendingIntent(context,
                    ACTION_FINISH_PLAN, position, flags++));
            itemViews.setTextViewText(R.id.tv_plan, plan.getContent());
            widgetView.addView(R.id.ll_all_plan, itemViews);
        }
        widgetView.setOnClickPendingIntent(R.id.iv_star, getPendingIntent(context, ACTION_USE_STAR, 0, flags++));
        widgetView.setOnClickPendingIntent(R.id.ll_all_plan, getStartActivityIntent(context, ACTION_ADD_PLAN, 0, flags++));
        widgetView.setTextViewText(R.id.tv_star_num, String.valueOf(person.getCurEncouragements()));
        appWidgetManager.updateAppWidget(id, widgetView);
    }
}
