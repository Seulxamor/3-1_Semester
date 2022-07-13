// 컴퓨터공학과 20184064 김정현
package com.example.calendardb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<CalendarItem> mcalendarItems; // CalendarItem 클래스의 타입을 갖는 arraylist 선언
    private Context mContext;
    private DBHelper mDBHelper;

    // 생성자
    public MyAdapter(ArrayList<CalendarItem> mcalendarItems, Context mContext) {
        this.mcalendarItems = mcalendarItems; 
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext); //DBHelper 객체 생성
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent,false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(mcalendarItems.get(position).getTitle());      // 일정 제목을 받아와 작성
        holder.contentView.setText(mcalendarItems.get(position).getContent());  // 일정 내용을 받아와 작성
        holder.dateview.setText(mcalendarItems.get(position).getWriteDate());   // 작성 날짜을 받아와 작성
    }

    @Override
    public int getItemCount() {
        return mcalendarItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView contentView;
        private TextView dateview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleView);      // 일정 제목 텍스트
            contentView = itemView.findViewById(R.id.contentVIew);  // 일정 내용 텍스트
            dateview = itemView.findViewById(R.id.dateView);        // 날짜 텍스트

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = getAdapterPosition(); // 현재 리스트 클릭 아이템 위치
                    CalendarItem calendarItem = mcalendarItems.get(currentPosition);

                    String[] strChoiceItems = {"수정하기", "삭제하기"}; // 수정하고 삭제가 가능하게 하기위해 배열 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext); // alertdialog를 사용하여 dialog를 띄움
                    builder.setTitle("어떤 작업을 하시겠습니까?");

                    // 아이템 클릭 이벤트
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {

                            if(pos == 0){ // 0번째 선택은 수정하기 옵션이됨
                                // Dialog 객체 생성
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);
                                EditText editTitle = dialog.findViewById(R.id.edit_title);
                                EditText editContent = dialog.findViewById(R.id.edit_content);

                                editTitle.setText(calendarItem.getTitle());     // 수정되기 전의 제목 값을 불러옴
                                editContent.setText(calendarItem.getContent()); // 수정되기 전의 내용 값을 불러옴

                                Button okBtn = dialog.findViewById(R.id.okBtn);

                                // 확인 버튼을 클릭 시 이벤트 처리
                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // 현재 날짜를 받아와서 DB에 넣어준다.
                                        // DB 수정하기
                                        String title = editTitle.getText().toString();
                                        String content = editContent.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                        String beforeTime = calendarItem.getWriteDate();
                                        mDBHelper.UpdateCalendar(title, content, currentTime, beforeTime);

                                        // 수정한 DB로 UI에 적용
                                        calendarItem.setTitle(title);
                                        calendarItem.setContent(content);
                                        calendarItem.setWriteDate(currentTime);
                                        notifyItemChanged(currentPosition, calendarItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "목록 수정 완료", Toast.LENGTH_SHORT).show(); // 수정이 완료되면 Toast 메시지 출력
                                    }
                                });

                                dialog.show();  // dialog를 띄워줌
                            }
                            else if (pos == 1) { // 1번째 선택은 삭제하기 옵션이됨
                                
                                // 삭제할 날짜를 DB에서 삭제
                                String beforeTime = calendarItem.getWriteDate();
                                mDBHelper.DleteCalendar(beforeTime);

                                // 삭제한 DB로 UI에 적용
                                mcalendarItems.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                Toast.makeText(mContext, "목록 제거 완료", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show(); // dialog를 띄워줌
                }
            });
        }
    }
    // 아이템을 추가해주는 메소드
    public void addItem(CalendarItem _item){
        mcalendarItems.add(0, _item); // 항상 0번째에 추가
        notifyItemInserted(0);
    }
}
