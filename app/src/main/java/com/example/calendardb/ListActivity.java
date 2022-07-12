//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRv_callist;   // RecyclerView
    private Button mAddBtn;             // Button
    private ArrayList<CalendarItem> mCalendarItems; // CalendarItem 클래스의 타입을 갖는 arraylist 선언
    private DBHelper mDBHelper;         // DBHelper
    private MyAdapter mAdapter;         // MyAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("일정 관리 어플");
        setInit();  // 시작하면 setInit() 실행
    }

    private void setInit() {
        mDBHelper = new DBHelper(this);
        mRv_callist = findViewById(R.id.rv_callist);
        mAddBtn = findViewById(R.id.addBtn);
        mCalendarItems = new ArrayList<>(); 

        loadRecentDB(); // 데이터베이스에서 현재 리스트 load

        // 추가하기 버튼 클릭시 이벤트 처리
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dialog 객체 생성
                Dialog dialog = new Dialog(ListActivity.this, android.R.style.Theme_Material_Light_Dialog); 
                dialog.setContentView(R.layout.dialog_edit);
                EditText editText = dialog.findViewById(R.id.edit_title);       // dialog 내에서 EditText를 사용
                EditText editContent = dialog.findViewById(R.id.edit_content);  // dialog 내에서 EdiText를 사용
                Button okBtn = dialog.findViewById(R.id.okBtn);

                // 확인 버튼을 클릭 시 이벤트 처리
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 현재 날짜를 받아오고 입력된 값을 DB에 삽입한다.
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        mDBHelper.InsertCalendar(editText.getText().toString(), editContent.getText().toString(), currentTime);

                        // DB에 삽입한 데이터를 UI에 띄워줌
                        CalendarItem item = new CalendarItem();
                        item.setTitle(editText.getText().toString());
                        item.setContent(editContent.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item); //아이템을 넘겨줌
                        mRv_callist.smoothScrollToPosition(0); //데이터가 올라갈때마다 스크롤을 이동
                        dialog.dismiss();   // dialog를 닫는다
                        Toast.makeText(ListActivity.this, "일정 추가 완료!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();  // dialog를 띄워줌
            }
        });
    }
    // 현재 DB에 저장되어있는 list들을 가져옴
    private void loadRecentDB() {
        mCalendarItems = mDBHelper.getCalendarlist();
        if (mAdapter == null){
            mAdapter = new MyAdapter(mCalendarItems, this);
            mRv_callist.setHasFixedSize(true);
            mRv_callist.setAdapter(mAdapter); // 리사이클러 뷰에 연결
        }
    }
}