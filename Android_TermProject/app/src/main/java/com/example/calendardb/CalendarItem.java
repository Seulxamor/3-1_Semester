//컴퓨터공학과 20184064 김정현
package com.example.calendardb;

public class CalendarItem {
    private int id;             // 일정 게시글 고유의 ID
    private String title;       // 일정 제목
    private String content;     // 일정 내용
    private String writeDate;   // 작성 날짜, 시간

    public CalendarItem() { }

    // id에 대한 접근자
    public int getId() {
        return id;
    }
    
    // id에 대한 설정자
    public void setId(int id) {
        this.id = id;
    }
    
    // 일정 제목에 대한 접근자
    public String getTitle() {
        return title;
    } 
    
    // 일정 제목에 대한 설정자
    public void setTitle(String title) {
        this.title = title;
    }
    
    // 일정 내용에 대한 접근자
    public String getContent() {
        return content;
    } 
    
    // 일정 내용에 대한 설정자
    public void setContent(String content) {
        this.content = content;
    }
    
    // 작성 날짜,시간에 대한 접근자
    public String getWriteDate() {
        return writeDate;
    } 
    
    // 작성 날짜,시간에 대한 설정자
    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}
