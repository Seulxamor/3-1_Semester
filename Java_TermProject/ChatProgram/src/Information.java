// ��ǻ�Ͱ��а� 20184064 ������
import java.io.*;

enum ConfirmData { // �����ִ� ������� �����Ͽ� ����
	connect, send, exit, join // ����, ����, ����
}

public class Information implements Serializable{ // �߿� ������ �������ִ� Ŭ����
	public static final long serialVersionUID = 1L;
	public String ClientName, Message; // ����� �̸�, �޽����� ���ڿ��� ����
	public ConfirmData data;
	
	// ����� �̸��� ��ȯ�ϴ� ������
	public String getClientName() {
		return ClientName;
	}
	// ����� �̸��� �����ϴ� ������
	public void setClientName(String clientName) {
		this.ClientName= clientName;
	}
	
	// � �������� ��ȯ�ϴ� ������
	public ConfirmData getData() {
		return data;
	}
	// � �������� �����ϴ� ������
	public void setData(ConfirmData data) {
		this.data= data;
	}
	
	// �޽����� ��ȯ�ϴ� ������
	public String getMessage() {
		return Message; 
	}
	// �޽����� �����ϴ� ������
	public void setMessage(String message) {
		this.Message= message; 
	}
	
	
	
	
	
}