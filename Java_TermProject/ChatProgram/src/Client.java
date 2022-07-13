// 컴퓨터공학과 20184064 김정현
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class Client extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	private JTextArea textArea, list_member;	// 채팅 textArea와 접속자 목록 list_member 선언
	private JTextField input;					// 메시지를 입력할 input 선언
	private JButton sendBtn, exitBtn;			// 전송하기, 종료하기 버튼
	private JScrollPane sp;						// 스크롤
	private Socket socket;						// 클라이언트 소켓
	private ObjectInputStream reader = null;	// 역직렬화 ObjectInputStream
	private ObjectOutputStream writer = null;	// 직렬화 ObjectOutputStream
	private String clientName;					// 사용자 이름
	private final int port = 9867;				// port 번호를 설정

	public Client() { // 생성자
		setTitle("클라이언트 채팅방"); // 창 제목 설정
		setSize(500, 410); 		  // 창 크기 설정
		setLayout(null);		  // 배치 관리자를 null로 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textArea = new JTextArea(""); 			  // JTextArea 객체 생성	
		textArea.setEditable(false);  			  // 수정 불가능
		textArea.setBackground(Color.LIGHT_GRAY); // 배경색 설정
		textArea.setFont(new Font("맑은 고딕", Font.BOLD, 15)); // 폰트 설정
		
		sp = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		sp.setBounds(4, 4, 340, 330);			 // 스크롤의 위치
		
		JLabel list = new JLabel("┌─접속자 목록─┐");
		list.setBounds(360,4,131,15);			 // 라벨의 위치 설정
		
		list_member = new JTextArea();
		list_member.setFont(new Font("맑은 고딕", Font.BOLD, 15)); // 폰트 설정
		list_member.setEditable(false);			 // 수정 불가능
		list_member.setBounds(349,20,131,314);   // list_member의 위치 설정
		
		input = new JTextField();				 // JTextFeild 객체 생성
		input.setBounds(4,339,273,25);			 // input의 위치 설정
		
		sendBtn = new JButton("전송하기");	
		sendBtn.setBackground(Color.LIGHT_GRAY); // 배경색 설정
		sendBtn.setForeground(Color.BLUE);		 // 버튼 내 글씨색 설정
		sendBtn.setBounds(278,339,100,25);		 // 전송하기 버튼의 위치 설정
		
		exitBtn = new JButton("종료하기");
		exitBtn.setBackground(Color.LIGHT_GRAY); // 배경색 설정
		exitBtn.setForeground(Color.BLACK);	     // 버튼 내 글씨색 설정
		exitBtn.setBounds(380,339,100,25);		 // 종료하기 버튼의 위치 설정
	
		add(sp);
		add(list);
		add(list_member);
		add(input);
		add(sendBtn);
		add(exitBtn);
		setVisible(true);  // 화면에 보여줌
		
		// 종료하기 버튼 클릭 시 이벤트 처리
		exitBtn.addActionListener(event -> {
			try {
				Information info = new Information();
				info.setClientName(clientName); // 종료하는 사용자 이름 설정
				info.setData(ConfirmData.exit); // 데이터를 종료 데이터로 설정
				writer.writeObject(info);  		// writer에 값을 보냄
				writer.flush();					// writer 값을 비워줌
			} catch (IOException e2) {			// 예외처리
				e2.printStackTrace();
			}
			
		});
		// 윈도우 이벤트 처리
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					Information info = new Information();
					info.setClientName(clientName); // 종료하는 사용자 이름 설정
					info.setData(ConfirmData.exit); // 데이터를 종료 데이터로 설정
					writer.writeObject(info); 		// writer에 값을 보냄
					writer.flush();					// writer 값을 비워줌
				} catch (IOException e2) {			// 예외처리
					e2.printStackTrace();
				}
			}
		});
	}
	
	public void ToServer() { // ToServer() 메소드
		// 표준 대화 상자를 사용하여 이름을 받음
		clientName = JOptionPane.showInputDialog(this, "이름을 입력하세요", "이름", 
				JOptionPane.QUESTION_MESSAGE);
		// 만약 받은 이름이 없거나 사용자가 입력한 값이 없다면 기본 이름을 익명으로 설정
		if (clientName == null || clientName.length() == 0) {
			clientName = "익명";
		}
		
		try {
			socket = new Socket("127.0.0.1", port); // Socket 객체 생성 (ip는 기본, port는 설정한 값)
			reader = new ObjectInputStream(socket.getInputStream());
			writer = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("[채팅 접속 성공]");
			System.out.println(clientName + "님이 접속하였습니다.\n"); // 사용자 접속시 콘솔에 출력
			
		} catch (IOException e) { // 예외 처리
			System.out.println("서버와 연결이 불가능합니다.");
			e.printStackTrace();
			System.exit(0);
		}

		try {
			// 서버로 이름 보내기
			Information info = new Information();
			info.setData(ConfirmData.connect);	// 데이터를 connect 데이터로 설정
			info.setClientName(clientName);		// 접속한 사용자의 이름 설정
			setTitle(info.getClientName() + "님의 채팅방" ); // 사용자 이름을 받아와 창 제목을 바꿈
			writer.writeObject(info); 			// writer에 값을 보냄
			writer.flush();						// writer 값을 비워줌
		} catch (IOException e) { // 예외 처리
			System.out.println("이름 전송 오류");
			e.printStackTrace();
		}
		
		try {
			// 서버로 이름 보내기
			Information info = new Information();
			info.setData(ConfirmData.join); // 데이터를 join 데이터로 설정
			info.setClientName(clientName); // 접속한 사용자의 이름 설정
			writer.writeObject(info); 		// writer에 값을 보냄
			writer.flush();					// writer 값을 비워줌
		} catch (IOException e) { // 예외 처리
			System.out.println("이름 전송 오류");
			e.printStackTrace();
		}
	
		Thread thread = new Thread(this); // 스레드 생성
		thread.start(); // 스레드 시작
		input.addActionListener(this);
		sendBtn.addActionListener(this);
	}

	@Override
	public void run() {
		// 서버로부터 데이터를 받음
		Information info = null;
		while (true) {
			try {
				info = (Information) reader.readObject();
				// 서버로부터 exit를 받으면 종료됨
				if (info.getData() == ConfirmData.exit) { 
					reader.close(); // reader 종료
					writer.close(); // writer 종료
					socket.close(); // 소켓 종료
					System.exit(0); // 강제 종료
				} 
				// 서버로부터 send를 받으면 메시지를 띄움
				else if (info.getData() == ConfirmData.send) {
					textArea.append(info.getMessage() + "\n"); // 메시지를 추가
					int pos = textArea.getText().length();
					textArea.setCaretPosition(pos);  
				} 
				// 서버로부터 join을 받으면 메시지를 띄움
				else if (info.getData() == ConfirmData.join) {	
					list_member.append(info.getClientName() + "\n"); // 접속자를 추가
				}
				
			} catch (IOException e) { 			 // 예외처리
				e.printStackTrace();
			} catch (ClassNotFoundException e) { // 예외처리
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			// 서버로 보냄
			// JTextField값을 서버로 보냄
			String msg = input.getText(); // 사용자의 채팅 메시지를 저장
			Information info = new Information();
			// 표준 대화 상자를 사용하여 오류 메시지를 띄워줌
			if (msg == null | msg.length() <= 0 ) {
				JOptionPane.showMessageDialog(this, "채팅전송오류", "전송오류", JOptionPane.ERROR_MESSAGE);
			}
			else {
				info.setData(ConfirmData.send);  // 데이터를 send 데이터로 설정
				info.setMessage(msg);			 // 입력한 메시지 설정
				info.setClientName(clientName);  // 접속한 사용자의 이름 설정
			}
			
			writer.writeObject(info);	// writer에 값을 보냄
			writer.flush(); 			// writer 값을 비워줌
			input.setText("");			// JTextField를 초기화

		} catch (IOException io) { // 예외처리
			io.printStackTrace();
		}
	}

}

