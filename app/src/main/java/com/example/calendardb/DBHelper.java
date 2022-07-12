//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "calendar.db";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스가 생성이 될 때 호출됨
        db.execSQL("CREATE TABLE IF NOT EXISTS calendar (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL  )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        onCreate(db);
    }

    // SELECT 문 (일정 목록들을 조회)
    public ArrayList<CalendarItem> getCalendarlist(){
        ArrayList<CalendarItem> calendarItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM calendar ORDER BY writeDate DESC", null); // 내림차순으로 정렬을 해서 가져옴
        if(cursor.getCount() != 0){
            // 조회온 데이터가 있을때 내부 수행
            // 다음으로 이동할 데이터가 없을 때까지 반복
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String writeDate = cursor.getString(cursor.getColumnIndexOrThrow("writeDate"));

                CalendarItem calendarItem = new CalendarItem();
                calendarItem.setId(id);
                calendarItem.setTitle(title);
                calendarItem.setContent(content);
                calendarItem.setWriteDate(writeDate);
                calendarItems.add(calendarItem);
            }
        }
        cursor.close(); // cursor를 닫음
        return calendarItems; // calendarItems를 반환
    }

    // INSERT 문 (일정 목록을 DB에 삽입)
    public void InsertCalendar(String _title, String _content, String _writeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO calendar (title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate +"')");
    }

    // UPDATE 문 (일정 목록을 DB에 수정)
    public void UpdateCalendar(String _title, String _content, String _writeDate, String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE calendar SET title = '" + _title + "', content = '" + _content  + "', writeDate = '" + _writeDate +  "'WHERE writeDate='" + _beforeDate + "'");
    }

    // DELETE 문 (일정 목록을 DB에서 제거)
    public void DleteCalendar(String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM calendar WHERE writeDate = '" + _beforeDate + "'");
    }
}
