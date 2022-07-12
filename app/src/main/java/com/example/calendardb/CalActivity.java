//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CalActivity extends AppCompatActivity {
    String fname = null;
    String str = null;
    Boolean is_empty = false;
    Boolean is_fun = false;
    CalendarView calendarView ;
    Button change_Btn, delete_Btn, save_Btn ;
    TextView dayText, calText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calactivity);
        setTitle("일정 관리 어플");
        calendarView = findViewById(R.id.calendarView);
        change_Btn = findViewById(R.id.change_Btn);
        delete_Btn = findViewById(R.id.delete_Btn);
        save_Btn = findViewById(R.id.save_Btn);
        dayText =findViewById(R.id.dayText);
        calText = findViewById(R.id.calText);

        //--------달력 클릭시 이벤트 처리--------
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                dayText.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                calText.setVisibility(View.INVISIBLE);
                change_Btn.setVisibility(View.INVISIBLE);
                delete_Btn.setVisibility(View.INVISIBLE);
                dayText.setText(String.format("%d / %d / %d", year, month+1, dayOfMonth));

                if (is_fun = false) {
                    save_Btn.setVisibility(View.VISIBLE);
                    change_Btn.setVisibility(View.INVISIBLE);
                    change_Btn.setVisibility(View.INVISIBLE);
                    is_empty = false;
                }
                checkDay(year, month, dayOfMonth);
            }
        });

        save_Btn.setOnClickListener(event->{

            AlertDialog.Builder ad = new AlertDialog.Builder(CalActivity.this);
            ad.setMessage("일정을 입력해주세요.");
            ad.setTitle("일정입력");

            final EditText et = new EditText(CalActivity.this);
            ad.setView(et);

            save_Btn.setVisibility(View.INVISIBLE);
            change_Btn.setVisibility(View.INVISIBLE);
            delete_Btn.setVisibility(View.INVISIBLE);
            //contextEditText.setVisibility(View.INVISIBLE);
            calText.setVisibility(View.VISIBLE);
            is_empty = false;
            is_fun = false;
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    str = et.getText().toString();
                    calText.setText(str);
                    saveDiary(fname, str);
                    dialogInterface.dismiss();
                }
            });

            ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            ad.show();
        });

    }

    public void  checkDay(int cYear,int cMonth,int cDay){
        fname=""+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정
        FileInputStream fi = null;//FileStream fis 변수



        try{
            fi = openFileInput(fname);

            byte[] fileData=new byte[fi.available()];
            fi.read(fileData);
            fi.close();


            str = new String(fileData); // 입력되어 일정 데이터를 str 변수에 저장
            //Toast.makeText(getApplicationContext(),str + "임", Toast.LENGTH_SHORT).show();
            //contextEditText.setVisibility(View.INVISIBLE);
            calText.setVisibility(View.VISIBLE);
            calText.setText(str);

            if(is_fun == false) {
                save_Btn.setVisibility(View.VISIBLE);
                change_Btn.setVisibility(View.INVISIBLE);
                delete_Btn.setVisibility(View.INVISIBLE);
            }

            if(is_empty == false) {
                save_Btn.setVisibility(View.INVISIBLE);
                change_Btn.setVisibility(View.VISIBLE);
                delete_Btn.setVisibility(View.VISIBLE);
            }
            //--------수정하기 버튼 이벤트 처리--------
            change_Btn.setOnClickListener(event->{
                AlertDialog.Builder ad = new AlertDialog.Builder(CalActivity.this);
                ad.setMessage("일정을 입력해주세요.");
                ad.setTitle("일정입력");

                final EditText et = new EditText(CalActivity.this);
                ad.setView(et);

                calText.setVisibility(View.VISIBLE);
                et.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                change_Btn.setVisibility(View.INVISIBLE);

                delete_Btn.setVisibility(View.INVISIBLE);
                calText.setText(et.getText());

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        str = et.getText().toString();
                        calText.setText(str);
                        saveDiary(fname, str);
                        calText.setVisibility(View.VISIBLE);
                        change_Btn.setVisibility(View.VISIBLE);
                        delete_Btn.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"수정 완료!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"수정 취소!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });

                ad.show();

            });

           //--------삭제하기 버튼 이벤트 처리--------
           delete_Btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   AlertDialog.Builder ad = new AlertDialog.Builder(CalActivity.this);
                   ad.setMessage("삭제하시겠습니까?");
                   ad.setTitle("일정 삭제");

                   //calText.setVisibility(View.INVISIBLE);
                   save_Btn.setVisibility(View.INVISIBLE);
                   change_Btn.setVisibility(View.INVISIBLE);
                   delete_Btn.setVisibility(View.INVISIBLE);
                   //contextEditText.setText("");
                   //contextEditText.setVisibility(View.INVISIBLE);

                   ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                           calText.setText("");
                           removeDiary(fname);
                           calText.setVisibility(View.INVISIBLE);
                           change_Btn.setVisibility(View.INVISIBLE);
                           delete_Btn.setVisibility(View.INVISIBLE);
                           is_empty = false;
                           Toast.makeText(getApplicationContext(),"삭제 완료!", Toast.LENGTH_SHORT).show();
                           dialogInterface.dismiss();
                       }
                   });

                   ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Toast.makeText(getApplicationContext(),"삭제 취소!", Toast.LENGTH_SHORT).show();
                           dialogInterface.dismiss();
                       }
                   });
                   ad.show();
               }
           });

        }catch (Exception e){ // 예외 처리
            e.printStackTrace();
        }
    }
    // 일정을 삭제하는 메서드
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fo = null; // 파일출력스트림 선언
        try{
            fo = openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content = ""; // content 변수에 공백을 저장
            fo.write((content).getBytes()); // 파일에 씀
            fo.close(); // 파일을 닫음

        }catch (Exception e){ // 예외 처리
            e.printStackTrace();
        }
    }
    // 일정을 저장하는 메서드
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay, String diary){
        FileOutputStream fo = null; // 파일출력스트림 선언
        try{
            fo = openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content = diary; // 받아온 일정을 content 변수에 저장
            fo.write((content).getBytes()); // 파일에 씀
            fo.close(); // 파일을 닫음
            
        }catch (Exception e){ // 예외 처리
            e.printStackTrace();
        }
    }
}
