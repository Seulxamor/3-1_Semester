//��ǻ�Ͱ��а� 20184064 ������
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
	private ServerSocket serverSocket; // ���� ����
	private Socket socket;  		   // Ŭ���̾�Ʈ ����
	private ChatHandler hd; 		   // �ڵ鷯
	public static JTextArea output;	   // textarea ��� 
	private final int port = 9867;	   // port ��ȣ�� ����
	private List <ChatHandler> list = new ArrayList<ChatHandler>(); //����Ʈ �迭
	
	public Server() { // ������
		setTitle("���� [20184064 ������]");	   // â ���� ����
		setSize(450, 300); // â ũ�� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		output = new JTextArea();
		output.setFont((new Font("���� ���",Font.BOLD,15)));
		output.setEditable(false); // ��� �Ұ���
		
		JScrollPane scroll = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
				,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ); // ��ũ���� textarea�� ����
		
		add(scroll);
		setVisible(true);

		try {
			serverSocket= new ServerSocket(port); 	// ���� port ����
			output.append("[���� �غ� �Ϸ�]\n");	  	// ���� �غ�Ϸ� ���
			System.out.println("[���� �غ� �Ϸ�]");  	// ���� �غ�Ϸ� ���
			while(true){
				socket = serverSocket.accept(); 	// ������ ��ٸ���, ������ �� ������ block���°� ��
				hd = new ChatHandler(socket, list); // CharHandler ��ü�� ����
				hd.start();  	// ������ ����
				list.add(hd);   // list�� ���� �ڵ鷯 ������ ������ Ŭ���̾�Ʈ ������ ��	
			}		
		} catch(IOException e){ // ����ó��
			output.append("[���� ���� ����]\n");
			System.out.println("[���� ���� ����]");
			e.printStackTrace();		
		}
	}
	
	public static void main(String[] args) {
		new Server();	// Server ��ü ����
	}
}

class ChatHandler extends Thread { 		// ���Ͽ� ���� ������ ����ְ� ó����
	private ObjectInputStream reader; 	// ��ü�� ������ȭ ObjectInputStream
	private ObjectOutputStream writer;	// ��ü�� ����ȭ ObjectOutputStream
	public Socket socket;
	public List <ChatHandler> list;

	public ChatHandler(Socket socket, List <ChatHandler> list) throws IOException { //������	
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
				clientName = info.getClientName(); 		// Ŭ���̾�Ʈ�� �̸��� �޾ƿ�

				// ����ڰ� ������ ������ ���
				if(info.getData() == ConfirmData.exit){
					Information sendinfo = new Information();
					sendinfo.setData(ConfirmData.exit); // �������� exit�� ���� Ŭ���̾�Ʈ���� exit ��������
					writer.writeObject(sendinfo); // writer�� ���� ����
					writer.flush(); // writer ���� �����

					reader.close();	// reader ����
					writer.close(); // writer ����
					socket.close();	// ���� ����
					list.remove(this);	// list�� ���� �����
					
					// ������ GUI�� ���� �����ߴ��� ���
					Server.output.append("[" + clientName + "���� " +
							socket.getPort()+"] ���� �����Ͽ����ϴ�.\n");
					
					// �����ִ� Ŭ���̾�Ʈ���� ����޼��� ����
					sendinfo.setData(ConfirmData.send);
					sendinfo.setMessage(clientName+"���� �����Ͽ����ϴ�");
					MsgForClient(sendinfo); // MsgForClient �Լ��� ������ ����
					break;
				}
				// ����ڰ� ���� ���� ���
				else if(info.getData() == ConfirmData.connect){
								
					Information sendinfo = new Information();
					// ������ GUI�� ���� �����ߴ��� ���
					Server.output.append("[" + clientName + "���� " +
							socket.getPort()+"] ���� �����Ͽ����ϴ�.\n");
					
					// ��� ����ڿ��� ���� �޼����� ����	
					sendinfo.setData(ConfirmData.send);
					sendinfo.setMessage(clientName+"���� �����Ͽ����ϴ�");
					sendinfo.setClientName(clientName);
					MsgForClient(sendinfo); // MsgForClient �Լ��� ������ ����
					
				}
				// ����ڰ� �޽����� ������ ���
				else if(info.getData()==ConfirmData.send){
					Information sendinfo = new Information();
					sendinfo.setData(ConfirmData.send);
					// ��� ����ڿ��� �̸��� �޽����� ����
					sendinfo.setMessage("["+clientName+"] "+ info.getMessage());					
					MsgForClient(sendinfo); // MsgForClient �Լ��� ������ ����
				}
				// ����ڰ� ���� �������
				else if (info.getData()==ConfirmData.join){
					Information sendinfo = new Information();
					sendinfo.setData(ConfirmData.join);
					// ��� ����ڿ��� �������� �̸��� ����
					sendinfo.setClientName(clientName);		
					MsgForClient(sendinfo);	// MsgForClient �Լ��� ������ ����
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
	
	//�ٸ� Ŭ���̾�Ʈ���� �Էµ� ��� �޼��� �����ֱ�
	public void MsgForClient(Information sendinfo) throws IOException {
		for(ChatHandler hd: list){
			hd.writer.writeObject(sendinfo); //�ڵ鷯 writer�� ���� ����
			hd.writer.flush();  //�ڵ鷯 writer ���� �����
		}
	}
}
