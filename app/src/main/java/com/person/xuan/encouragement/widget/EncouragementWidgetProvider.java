package com.person.xuan.encouragement.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.activity.HomeActivity;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.entity.Plan;

import java.util.Set;

/**
 * Created by chenxiaoxuan1 on 15/12/11.
 */
public class EncouragementWidgetProvider extends AppWidgetProvider {
    private String TAG = "EncouragementWidgetProvider";
    private static final int ACTION_USE_STAR = 1001;
    private static final int ACTION_FINISH_PLAN = 1002;
    private static final int ACTION_ADD_PLAN = 1003;
    private static final String ACTION = "ACTION";
    private static final String POSITION = "POSITION";

    /*
     * 在3种情况下会调用OnUpdate()。onUpdate()是在main线程中进行，因此如果处理需要花费时间多于10秒，处理应在service中完成。
     *（1）在时间间隔到时调用，时间间隔在widget定义的android:updatePeriodMillis中设置；
     *（2）用户拖拽到主页，widget实例生成。无论有没有设置Configure activity，我们在Android4.4的测试中，当用户拖拽图片至主页时，widget实例生成，会触发onUpdate()，然后再显示activity（如果有）。这点和资料说的不一样，资料认为如果设置了Configure acitivity，就不会在一开始调用onUpdate()，而实验显示当实例生成（包括创建和重启时恢复），都会先调用onUpate()。在本例，由于此时在preference尚未有相关数据，创建实例时不能有效进行数据设置。
     *（3）机器重启，实例在主页上显示，会再次调用onUpdate()
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.e(TAG, "onUpdate appWidgetIds : " + appWidgetIds);
        Set<Integer> allWidgetIds = EncouragementWrapper.getWidgetIds(context);
        for (int appWidgetId : appWidgetIds) {
            allWidgetIds.add(Integer.valueOf(appWidgetId));
        }
        EncouragementWrapper.writeWidgetId(context, allWidgetIds);
        refreshView(context, allWidgetIds, appWidgetManager, EncouragementWrapper.getPerson(context));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    // 接收广播的回调函数
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
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
        super.onReceive(context, intent);
    }

    private PendingIntent getStartActivityIntent(Context context, int action, int position, int flag) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        //flags 最好完全不同,并且不能使用负数
        PendingIntent pendingIntent = PendingIntent.getActivity(context, action, intent, flag);
        return pendingIntent;
    }

    private PendingIntent getPendingIntent(Context context, int action, int position, int flag) {
        Intent intent = new Intent();
        intent.setClass(context, EncouragementWidgetProvider.class);
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


    // widget被删除时调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted(): appWidgetIds.length=" + appWidgetIds.length);
        super.onDeleted(context, appWidgetIds);
    }

    // 最后一个widget被删除时调用
    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled");

        super.onDisabled(context);
    }

    // 第一个widget被创建时调用
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

}
