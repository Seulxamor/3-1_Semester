// 컴퓨터공학과 20184064 김정현
import java.io.*;

enum ConfirmData { // 관련있는 상수들을 열거하여 선언
	connect, send, exit, join // 연결, 수신, 종료
}

public class Information implements Serializable{ // 중요 정보를 전달해주는 클래스
	public static final long serialVersionUID = 1L;
	public String ClientName, Message; // 사용자 이름, 메시지를 문자열로 선언
	public ConfirmData data;
	
	// 사용자 이름을 반환하는 접근자
	public String getClientName() {
		return ClientName;
	}
	// 사용자 이름을 설정하는 설정자
	public void setClientName(String clientName) {
		this.ClientName= clientName;
	}
	
	// 어떤 데이턴지 반환하는 접근자
	public ConfirmData getData() {
		return data;
	}
	// 어떤 데이턴지 설정하는 설정자
	public void setData(ConfirmData data) {
		this.data= data;
	}
	
	// 메시지를 반환하는 접근자
	public String getMessage() {
		return Message; 
	}
	// 메시지를 설정하는 설정자
	public void setMessage(String message) {
		this.Message= message; 
	}
	
	
	
	
	
}