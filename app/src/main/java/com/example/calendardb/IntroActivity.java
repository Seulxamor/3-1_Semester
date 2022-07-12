//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intropage);

        ImageButton start = (ImageButton)findViewById(R.id.startBtn);

        // ----------시작하기 버튼 클릭 시 이벤트 처리----------
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, SelectActivity.class); // SelectActivity 클래스로 인텐트 설정
                startActivity(intent); // 인텐트 실행
            }
        });
    }

}
