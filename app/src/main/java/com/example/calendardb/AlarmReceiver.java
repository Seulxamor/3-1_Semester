//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmService alarmService = new AlarmService(context); // AlarmService 객체 생성
        NotificationCompat.Builder nobuilder = alarmService.getChannlNotification();
        alarmService.getManager().notify(1, nobuilder.build());
    }
}
