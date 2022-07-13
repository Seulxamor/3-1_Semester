// ��ǻ�Ͱ��а� 20184064 ������
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class Client extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	private JTextArea textArea, list_member;	// ä�� textArea�� ������ ��� list_member ����
	private JTextField input;					// �޽����� �Է��� input ����
	private JButton sendBtn, exitBtn;			// �����ϱ�, �����ϱ� ��ư
	private JScrollPane sp;						// ��ũ��
	private Socket socket;						// Ŭ���̾�Ʈ ����
	private ObjectInputStream reader = null;	// ������ȭ ObjectInputStream
	private ObjectOutputStream writer = null;	// ����ȭ ObjectOutputStream
	private String clientName;					// ����� �̸�
	private final int port = 9867;				// port ��ȣ�� ����

	public Client() { // ������
		setTitle("Ŭ���̾�Ʈ ä�ù�"); // â ���� ����
		setSize(500, 410); 		  // â ũ�� ����
		setLayout(null);		  // ��ġ �����ڸ� null�� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textArea = new JTextArea(""); 			  // JTextArea ��ü ����	
		textArea.setEditable(false);  			  // ���� �Ұ���
		textArea.setBackground(Color.LIGHT_GRAY); // ���� ����
		textArea.setFont(new Font("���� ���", Font.BOLD, 15)); // ��Ʈ ����
		
		sp = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		sp.setBounds(4, 4, 340, 330);			 // ��ũ���� ��ġ
		
		JLabel list = new JLabel("���������� ��Ϧ���");
		list.setBounds(360,4,131,15);			 // ���� ��ġ ����
		
		list_member = new JTextArea();
		list_member.setFont(new Font("���� ���", Font.BOLD, 15)); // ��Ʈ ����
		list_member.setEditable(false);			 // ���� �Ұ���
		list_member.setBounds(349,20,131,314);   // list_member�� ��ġ ����
		
		input = new JTextField();				 // JTextFeild ��ü ����
		input.setBounds(4,339,273,25);			 // input�� ��ġ ����
		
		sendBtn = new JButton("�����ϱ�");	
		sendBtn.setBackground(Color.LIGHT_GRAY); // ���� ����
		sendBtn.setForeground(Color.BLUE);		 // ��ư �� �۾��� ����
		sendBtn.setBounds(278,339,100,25);		 // �����ϱ� ��ư�� ��ġ ����
		
		exitBtn = new JButton("�����ϱ�");
		exitBtn.setBackground(Color.LIGHT_GRAY); // ���� ����
		exitBtn.setForeground(Color.BLACK);	     // ��ư �� �۾��� ����
		exitBtn.setBounds(380,339,100,25);		 // �����ϱ� ��ư�� ��ġ ����
	
		add(sp);
		add(list);
		add(list_member);
		add(input);
		add(sendBtn);
		add(exitBtn);
		setVisible(true);  // ȭ�鿡 ������
		
		// �����ϱ� ��ư Ŭ�� �� �̺�Ʈ ó��
		exitBtn.addActionListener(event -> {
			try {
				Information info = new Information();
				info.setClientName(clientName); // �����ϴ� ����� �̸� ����
				info.setData(ConfirmData.exit); // �����͸� ���� �����ͷ� ����
				writer.writeObject(info);  		// writer�� ���� ����
				writer.flush();					// writer ���� �����
			} catch (IOException e2) {			// ����ó��
				e2.printStackTrace();
			}
			
		});
		// ������ �̺�Ʈ ó��
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					Information info = new Information();
					info.setClientName(clientName); // �����ϴ� ����� �̸� ����
					info.setData(ConfirmData.exit); // �����͸� ���� �����ͷ� ����
					writer.writeObject(info); 		// writer�� ���� ����
					writer.flush();					// writer ���� �����
				} catch (IOException e2) {			// ����ó��
					e2.printStackTrace();
				}
			}
		});
	}
	
	public void ToServer() { // ToServer() �޼ҵ�
		// ǥ�� ��ȭ ���ڸ� ����Ͽ� �̸��� ����
		clientName = JOptionPane.showInputDialog(this, "�̸��� �Է��ϼ���", "�̸�", 
				JOptionPane.QUESTION_MESSAGE);
		// ���� ���� �̸��� ���ų� ����ڰ� �Է��� ���� ���ٸ� �⺻ �̸��� �͸����� ����
		if (clientName == null || clientName.length() == 0) {
			clientName = "�͸�";
		}
		
		try {
			socket = new Socket("127.0.0.1", port); // Socket ��ü ���� (ip�� �⺻, port�� ������ ��)
			reader = new ObjectInputStream(socket.getInputStream());
			writer = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("[ä�� ���� ����]");
			System.out.println(clientName + "���� �����Ͽ����ϴ�.\n"); // ����� ���ӽ� �ֿܼ� ���
			
		} catch (IOException e) { // ���� ó��
			System.out.println("������ ������ �Ұ����մϴ�.");
			e.printStackTrace();
			System.exit(0);
		}

		try {
			// ������ �̸� ������
			Information info = new Information();
			info.setData(ConfirmData.connect);	// �����͸� connect �����ͷ� ����
			info.setClientName(clientName);		// ������ ������� �̸� ����
			setTitle(info.getClientName() + "���� ä�ù�" ); // ����� �̸��� �޾ƿ� â ������ �ٲ�
			writer.writeObject(info); 			// writer�� ���� ����
			writer.flush();						// writer ���� �����
		} catch (IOException e) { // ���� ó��
			System.out.println("�̸� ���� ����");
			e.printStackTrace();
		}
		
		try {
			// ������ �̸� ������
			Information info = new Information();
			info.setData(ConfirmData.join); // �����͸� join �����ͷ� ����
			info.setClientName(clientName); // ������ ������� �̸� ����
			writer.writeObject(info); 		// writer�� ���� ����
			writer.flush();					// writer ���� �����
		} catch (IOException e) { // ���� ó��
			System.out.println("�̸� ���� ����");
			e.printStackTrace();
		}
	
		Thread thread = new Thread(this); // ������ ����
		thread.start(); // ������ ����
		input.addActionListener(this);
		sendBtn.addActionListener(this);
	}

	@Override
	public void run() {
		// �����κ��� �����͸� ����
		Information info = null;
		while (true) {
			try {
				info = (Information) reader.readObject();
				// �����κ��� exit�� ������ �����
				if (info.getData() == ConfirmData.exit) { 
					reader.close(); // reader ����
					writer.close(); // writer ����
					socket.close(); // ���� ����
					System.exit(0); // ���� ����
				} 
				// �����κ��� send�� ������ �޽����� ���
				else if (info.getData() == ConfirmData.send) {
					textArea.append(info.getMessage() + "\n"); // �޽����� �߰�
					int pos = textArea.getText().length();
					textArea.setCaretPosition(pos);  
				} 
				// �����κ��� join�� ������ �޽����� ���
				else if (info.getData() == ConfirmData.join) {	
					list_member.append(info.getClientName() + "\n"); // �����ڸ� �߰�
				}
				
			} catch (IOException e) { 			 // ����ó��
				e.printStackTrace();
			} catch (ClassNotFoundException e) { // ����ó��
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			// ������ ����
			// JTextField���� ������ ����
			String msg = input.getText(); // ������� ä�� �޽����� ����
			Information info = new Information();
			// ǥ�� ��ȭ ���ڸ� ����Ͽ� ���� �޽����� �����
			if (msg == null | msg.length() <= 0 ) {
				JOptionPane.showMessageDialog(this, "ä�����ۿ���", "���ۿ���", JOptionPane.ERROR_MESSAGE);
			}
			else {
				info.setData(ConfirmData.send);  // �����͸� send �����ͷ� ����
				info.setMessage(msg);			 // �Է��� �޽��� ����
				info.setClientName(clientName);  // ������ ������� �̸� ����
			}
			
			writer.writeObject(info);	// writer�� ���� ����
			writer.flush(); 			// writer ���� �����
			input.setText("");			// JTextField�� �ʱ�ȭ

		} catch (IOException io) { // ����ó��
			io.printStackTrace();
		}
	}

}

