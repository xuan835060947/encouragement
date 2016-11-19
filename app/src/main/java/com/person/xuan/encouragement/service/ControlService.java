package com.person.xuan.encouragement.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.person.xuan.encouragement.data.HistoryData;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.util.ShareValueUtil;
import com.person.xuan.encouragement.widget.EncouragementWrapper;

/**
 * Created by chenxiaoxuan1 on 16/1/4.
 */
public class ControlService extends Service {
    private String TAG = ControlService.class.getSimpleName();
    private static final int ACTION_USE_STAR = 1001;
    private static final int ACTION_FINISH_PLAN = 1002;
    private static final int ACTION_ADD_PLAN = 1003;
    private static final String KEY_ACTION = ShareValueUtil.KEY_ACTION;
    private static final String KEY_ID = ShareValueUtil.KEY_ID;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand flags " + flags + "  startId " + startId);
        int action = intent.getIntExtra(KEY_ACTION, -1);
        long id = intent.getLongExtra(KEY_ID, -1);
        Person person = EncouragementWrapper.getPerson(this);
        Log.e(TAG, "onStartCommand " + "action : " + action + " id : " + id + " plan size : " + person.getPlans().size());
        if (action == ACTION_USE_STAR) {
            if (person.usedOneEncouragements()) {
                Toast.makeText(this, "使用一颗星", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "星星不足,快完成点计划吧!", Toast.LENGTH_SHORT).show();
            }
        } else if (action == ACTION_FINISH_PLAN) {
            HistoryData.addHistoryPlan(person.getPlan(id));
            person.finish(id);
        }
        EncouragementWrapper.writePerson(this, person);

        Intent update = new Intent();
        update.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        update.putExtra(KEY_ACTION, action);
        intent.putExtra(KEY_ID, id);
        sendBroadcast(update);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

}
