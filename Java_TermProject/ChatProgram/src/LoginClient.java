// ��ǻ�Ͱ��а� 20184064 ������
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class LoginClient extends JFrame { // �α��� Ŭ����
	private static final long serialVersionUID = 1L;

	final int margin = 40; // ������ 40���� ����
	private JLabel id, pw; // ���̵�, ��й�ȣ
	private JTextField idField, pwField; // ���̵�, ��й�ȣ�� JTextField�� �Է� ����
	private JButton loginBtn; // �α��� ��ư
	private ResultSet rs;
	private Statement stmt;
	Map<String, Integer> map = new HashMap<String, Integer>();
	
	public LoginClient() throws SQLException { // ������
		setTitle("�α��� ȭ��"); // â ���� ����
		Connection con = makeConnection(); 
		stmt = con.createStatement(); // SQL ���� �ۼ� �� ����
		rs = stmt.executeQuery("SELECT * FROM user_list");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(250, 180); // â ũ�� ����
		setLayout(null); // ��ġ�����ڸ� null�� ����

		id = new JLabel("���̵� :"); // ���̵�
		id.setBounds(40, 20 + margin * 0, 70, 30); // ���̵� label�� ��ġ ����
		id.setHorizontalAlignment(JLabel.LEFT); // ���̵� label�� ���� ����

		idField = new JTextField(20); // ���̵� �Է¹޴� ũ�� 20�� TextField
		idField.setBounds(100, 25 + margin * 0, 70, 20); // TextField�� ��ġ ����

		pw = new JLabel("��й�ȣ :"); // ��й�ȣ
		pw.setBounds(40, 50 + margin * 0, 100, 30); // ��й�ȣ label�� ��ġ ����
		pw.setHorizontalAlignment(JLabel.LEFT); // ��й�ȣ label�� ���� ����

		pwField = new JPasswordField(20); // ��й�ȣ�� �Է¹޴� ũ�� 20�� TextField
		pwField.setBounds(100, 55 + margin * 0, 80, 20); // TextField�� ��ġ ����

		loginBtn = new JButton("Login"); // �α��� ��ư
		loginBtn.setBounds(40, 90 + margin * 0, 140, 30); // �α��� ��ư�� ��ġ ����

		add(id); // ���̵� �� �߰�
		add(idField); // ���̵� TextField �߰�
		add(pw); // ��й�ȣ �� �߰�
		add(pwField); // ��й�ȣ TextField �߰�
		add(loginBtn); // �α��� ��ư �߰�
		setVisible(true); // ȭ�鿡 ���̰���

		// �α��� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ ó��
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String getid = idField.getText().trim(); // ���ڿ� ���� id ���� �� TextField�� ���� ���̵� ���� �ҷ��� ����
				String getpw = pwField.getText().trim(); // ���ڿ� ���� pw ���� �� TextField�� ���� ��й�ȣ ���� �ҷ��� ����
				int pw = Integer.parseInt(getpw);		 // ���ڿ��� ���� pw�� ���������� ����ȯ
		
				try {
					while (rs.next()) {
						String db_id = rs.getString("id"); // ������ ���̽��� ����� ���̵� db_id�� ����
						int db_pw = rs.getInt("pw");	   // ������ ���̽��� ����� pw db_pw�� ����
						map.put(db_id, db_pw);			   // map�� id�� pw�� ����
					}
				} catch (SQLException e1) { // ����ó��
					e1.printStackTrace();
				}
			
				Set<String> keys = map.keySet(); // map�� ����� Ű���� ����
				
				for (String key : keys) {
					// ���� ��� Ű���� value �����߿� ������ id�� pw�� ������ �α��� ����
					if ((getid.equals(key)) && pw == map.get(key)){
						JOptionPane.showMessageDialog(null, "�α��� ����!",
								"�α���",JOptionPane.DEFAULT_OPTION); 
						new Client().ToServer(); // Ŭ���̾�Ʈ ä�� ����
					}
					// ���� ��� Ű���� value �����߿� id�� pw�� �������� ������ �α��� ����
					if ((getid.equals(key)) && pw != map.get(key) || (!getid.equals(key)) && pw != map.get(key)) {
						JOptionPane.showMessageDialog(null, "�α��� ����", "�α���",
								 JOptionPane.ERROR_MESSAGE); 
						return; 
					}		
				}	
			}
		});

	}

	public static Connection makeConnection() {
		String url = "jdbc:mysql://localhost:3306/login_db?chracterEncoding = UTF-8 & serverTimezone=UTC";

		String id = "root";	// �����ͺ��̽��� id
		String password = "1q2w3e4r!"; // �����ͺ��̽� pw
		Connection con = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // JABC ����̹��� ����
			System.out.println("����̹� ���� ����");
			con = DriverManager.getConnection(url, id, password); // �����ͺ��̽� ����
			System.out.println("�����ͺ��̽� ���� ����");
		} catch (ClassNotFoundException e) {	// ����ó��
			System.out.println("����̹��� ã�� �� �����ϴ�.");
		} catch (SQLException e) {				// ����ó��
			System.out.println("���ῡ �����Ͽ����ϴ�.");
		}
		return con;
	}

	public static void main(String[] args) throws SQLException {
		new LoginClient(); // LoginClient ��ü ����
	}
}
