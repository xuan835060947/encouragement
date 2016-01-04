package com.person.xuan.encouragement.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class ConfigurationChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "监测到转屏广播", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
            Toast.makeText(context, "监测到转屏广播,拦截", Toast.LENGTH_SHORT).show();
            Bundle bundle = getResultExtras(true);
            if (bundle != null) {
                Toast.makeText(context, "bundle is not null", Toast.LENGTH_SHORT).show();
            }
            //取消广播
            abortBroadcast();
        }
    }
}