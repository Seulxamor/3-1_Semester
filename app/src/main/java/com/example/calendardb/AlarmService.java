//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends ContextWrapper {
    public static final String channelID = "channelID"; // 채널의 ID 선언
    public static final String channelNAME = "channelNAME"; // 채널의 이름 선언
    private NotificationManager notiManager;

    public AlarmService(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 버전에 맞으면
            createChannels(); // 채널을 생성
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channel = new NotificationChannel(channelID, channelNAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(com.google.android.material.R.color.design_default_color_on_primary);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if(notiManager == null){
            notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notiManager; // notiManger를 반환한다.
    }

    public NotificationCompat.Builder getChannlNotification(){
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("알림!")                    // 알림의 제목
                .setContentText("설정하신 시간이 되었습니다!")   // 알림의 내용
                .setSmallIcon(R.drawable.notice);           // 알림의 아이콘
    }
}