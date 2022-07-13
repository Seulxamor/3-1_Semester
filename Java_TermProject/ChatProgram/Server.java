//컴퓨터공학과 20184064 김정현
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class Server extends JFrame {
	private static final long serialVersionUID = 1L;
	private ServerSocket serverSocket; // 서버 소켓
	private Socket socket;  		   // 클라이언트 소켓
	private ChatHandler hd; 		   // 핸들러
	public static JTextArea output;	   // textarea 사용 
	private final int port = 9867;	   // port 번호를 설정
	private List <ChatHandler> list = new ArrayList<ChatHandler>(); //리스트 배열
	
	public Server() { // 생성자
		setTitle("서버 [20184064 김정현]");	   // 창 제목 설정
		setSize(450, 300); // 창 크기 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		output = new JTextArea();
		output.setFont((new Font("맑은 고딕",Font.BOLD,15)));
		output.setEditable(false); // 사용 불가능
		
		JScrollPane scroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
				,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ); // 스크롤을 textarea에 설정
		
		add(scroll);
		setVisible(true);

		try {
			serverSocket= new ServerSocket(port); 	// 서버 port 선언
			output.append("[서버 준비 완료]\n");	  	// 서버 준비완료 출력
			System.out.println("[서버 준비 완료]");  	// 서버 준비완료 출력
			while(true){
				socket = serverSocket.accept(); 	// 연결을 기다리며, 연결이 될 때까지 block상태가 됨
				hd = new ChatHandler(socket, list); // CharHandler 객체를 생성
				hd.start();  	// 스레드 시작
				list.add(hd);   // list에 들어가는 핸들러 개수가 접속한 클라이언트 개수가 됨	
			}		
		} catch(IOException e){ // 예외처리
			output.append("[서버 접속 오류]\n");
			System.out.println("[서버 접속 오류]");
			e.printStackTrace();		
		}
	}
	
	public static void main(String[] args) {
		new Server();	// Server 객체 생성
	}
}

class ChatHandler extends Thread { 		// 소켓에 대한 정보가 담겨있고 처리함
	private ObjectInputStream reader; 	// 객체의 역직렬화 ObjectInputStream
	private ObjectOutputStream writer;	// 객체의 직렬화 ObjectOutputStream
	public Socket socket;
	public List <ChatHandler> list;

	public ChatHandler(Socket socket, List <ChatHandler> list) throws IOException { //생성자	
		this.socket = socket;
		this.list = list;
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
	}
	public void run() {
		Information info = null;
		String clientName;
		try{
			while(true){
				info=(Information)reader.readObject();
				clientName = info.getClientName(); 		// 클라이언트의 이름을 받아옴

				// 사용자가 접속을 끊었을 경우
				if(info.getData() == ConfirmData.exit){
					Information sendinfo = new Information();
					sendinfo.setData(ConfirmData.exit); // 나가려고 exit를 보낸 클라이언트에게 exit 설정해줌
					writer.writeObject(sendinfo); // writer에 값을 보냄
					writer.flush(); // writer 값을 비워줌

					reader.close();	// reader 종료
					writer.close(); // writer 종료
					socket.close();	// 소켓 종료
					list.remove(this);	// list의 값을 비워줌
					
					// 서버의 GUI에 누가 퇴장했는지 출력
					Server.output.append("[" + clientName + "님이 " +
							socket.getPort()+"] 에서 퇴장하였습니다.\n");
					
					// 남아있는 클라이언트에게 퇴장메세지 보냄
					sendinfo.setData(ConfirmData.send);
					sendinfo.setMessage(clientName+"님이 퇴장하였습니다");
					MsgForClient(sendinfo); // MsgForClient 함수로 정보를 보냄
					break;
				}
				// 사용자가 접속 했을 경우
				else if(info.getData() == ConfirmData.connect){
								
					Information sendinfo = new Information();
					// 서버의 GUI에 누가 입장했는지 출력
					Server.output.append("[" + clientName + "님이 " +
							socket.getPort()+"] 에서 입장하였습니다.\n");
					
					// 모든 사용자에게 입장 메세지를 보냄	
					sendinfo.setData(ConfirmData.send);
					sendinfo.setMessage(clientName+"님이 입장하였습니다");
					sendinfo.setClientName(clientName);
					MsgForClient(sendinfo); // MsgForClient 함수로 정보를 보냄
					
				}
				// 사용자가 메시지를 보냈을 경우
				else if(info.getData()==ConfirmData.send){
					Information sendinfo = new Information();
					sendinfo.setData(ConfirmData.send);
					// 모든 사용자에게 이름과 메시지를 보냄
					sendinfo.setMessage("["+clientName+"] "+ info.getMessage());					
					MsgForClient(sendinfo); // MsgForClient 함수로 정보를 보냄
				}
				// 사용자가 참여 했을경우
				else if (info.getData()==ConfirmData.join){
					Information sendinfo = new Information();
					sendinfo.setData(ConfirmData.join);
					// 모든 사용자에게 접속자의 이름을 보냄
					sendinfo.setClientName(clientName);		
					MsgForClient(sendinfo);	// MsgForClient 함수로 정보를 보냄
				}	
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//다른 클라이언트에게 입력된 모든 메세지 보내주기
	public void MsgForClient(Information sendinfo) throws IOException {
		for(ChatHandler hd: list){
			hd.writer.writeObject(sendinfo); //핸들러 writer에 값을 보냄
			hd.writer.flush();  //핸들러 writer 값을 비워줌
		}
	}
}
