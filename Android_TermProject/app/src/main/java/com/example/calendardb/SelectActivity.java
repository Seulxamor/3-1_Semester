// 컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SelectActivity extends AppCompatActivity {
    ImageButton cal, list, notice; // 페이지를 선택할 수 있는 이미지 버튼들을 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // 각각의 이미지 버튼을 사용하기위해 이름을 가져와 저장
        cal = (ImageButton)findViewById(R.id.cal);          
        list = (ImageButton)findViewById(R.id.list);        
        notice = (ImageButton)findViewById(R.id.notice);

        // ----------달력 이미지 버튼 클릭 시 이벤트 처리----------
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, CalActivity.class); // CalActivity 클래스로 인텐트 설정
                startActivity(intent); // 인텐트 페이지 시작
            }
        });
        
        // ----------리스트 이미지 버튼 클릭 시 이벤트 처리----------
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ListActivity.class); // ListActivity 클래스로 인텐트 설정
                startActivity(intent); // 인텐트 페이지 시작
            }
        });
        
        // ----------알림 이미지 버튼 클릭 시 이벤트 처리----------
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, AlarmActivity.class); // AlarmActivity 클래스로 인텐트 설정
                startActivity(intent); // 인텐트 페이지 시작
            }
        });
    }

}
