//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {
    private Calendar calendar;
    private TimePicker time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmlayout);

        this.calendar = Calendar.getInstance();
        displayDate(); // 날짜 표시

        this.time = findViewById(R.id.timepicker); // TimePicker 변수 time 사용
        findViewById(R.id.calendarBtn).setOnClickListener(mClickListener);
        findViewById(R.id.alarmBtn).setOnClickListener(mClickListener);

    }

    private void displayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ((TextView) findViewById(R.id.textDate)).setText(format.format(this.calendar.getTime()));
    }

    private void showDatePicker(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //알람 날짜 설정
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, day);
                displayDate(); // 날짜 표시
            }
        }, this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH) , this.calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show(); // dialog를 띄워줌
    }

    @SuppressLint("NewApi")
    private void setAlarm(){
        // 알람 시간 설정
        this.calendar.set(Calendar.HOUR_OF_DAY, this.time.getHour());
        this.calendar.set(Calendar.MINUTE, this.time.getMinute());
        this.calendar.set(Calendar.SECOND, 0);

        if(this.calendar.before(Calendar.getInstance())){
            // 만약 지난 시간을 클릭하면 Toast 메시지 출력
            Toast.makeText(this, "지난 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 인텐트 객체 생성, AlarmReceiver 클래스로 넘어감
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알람을 설정함
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), pendingIntent);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Toast.makeText(this, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_LONG).show();

    }

    View.OnClickListener mClickListener = (v)->{
        switch (v.getId()){ // 클릭한 버튼의 ID가 달력보기 버튼이면 
            case R.id.calendarBtn:
                showDatePicker(); // 달력을 보여줌
                break;
            case R.id.alarmBtn: // 알람 버튼이면
                setAlarm();     // 알람 설정
                break;
        }
    };
}
