// 컴퓨터공학과 20184064 김정현
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

class LoginClient extends JFrame { // 로그인 클래스
	private static final long serialVersionUID = 1L;

	final int margin = 40; // 마진을 40으로 고정
	private JLabel id, pw; // 아이디, 비밀번호
	private JTextField idField, pwField; // 아이디, 비밀번호를 JTextField로 입력 받음
	private JButton loginBtn; // 로그인 버튼
	private ResultSet rs;
	private Statement stmt;
	Map<String, Integer> map = new HashMap<String, Integer>();
	
	public LoginClient() throws SQLException { // 생성자
		setTitle("로그인 화면"); // 창 제목 설정
		Connection con = makeConnection(); 
		stmt = con.createStatement(); // SQL 문장 작성 및 전송
		rs = stmt.executeQuery("SELECT * FROM user_list");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(250, 180); // 창 크기 설정
		setLayout(null); // 배치관리자를 null로 설정

		id = new JLabel("아이디 :"); // 아이디
		id.setBounds(40, 20 + margin * 0, 70, 30); // 아이디 label의 위치 설정
		id.setHorizontalAlignment(JLabel.LEFT); // 아이디 label을 왼쪽 정렬

		idField = new JTextField(20); // 아이디를 입력받는 크기 20의 TextField
		idField.setBounds(100, 25 + margin * 0, 70, 20); // TextField의 위치 설정

		pw = new JLabel("비밀번호 :"); // 비밀번호
		pw.setBounds(40, 50 + margin * 0, 100, 30); // 비밀번호 label의 위치 설정
		pw.setHorizontalAlignment(JLabel.LEFT); // 비밀번호 label을 왼쪽 정렬

		pwField = new JPasswordField(20); // 비밀번호를 입력받는 크기 20의 TextField
		pwField.setBounds(100, 55 + margin * 0, 80, 20); // TextField의 위치 설정

		loginBtn = new JButton("Login"); // 로그인 버튼
		loginBtn.setBounds(40, 90 + margin * 0, 140, 30); // 로그인 버튼의 위치 설정

		add(id); // 아이디 라벨 추가
		add(idField); // 아이디 TextField 추가
		add(pw); // 비밀번호 라벨 추가
		add(pwField); // 비밀번호 TextField 추가
		add(loginBtn); // 로그인 버튼 추가
		setVisible(true); // 화면에 보이게함

		// 로그인 버튼 클릭시 발생하는 이벤트 처리
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String getid = idField.getText().trim(); // 문자열 변수 id 선언 후 TextField에 적은 아이디 값을 불러와 저장
				String getpw = pwField.getText().trim(); // 문자열 변수 pw 선언 후 TextField에 적은 비밀번호 값을 불러와 저장
				int pw = Integer.parseInt(getpw);		 // 문자열로 받은 pw를 정수형으로 형변환
		
				try {
					while (rs.next()) {
						String db_id = rs.getString("id"); // 데이터 베이스에 저장된 아이디를 db_id에 저장
						int db_pw = rs.getInt("pw");	   // 데이터 베이스에 저장된 pw db_pw에 저장
						map.put(db_id, db_pw);			   // map에 id와 pw를 매핑
					}
				} catch (SQLException e1) { // 예외처리
					e1.printStackTrace();
				}
			
				Set<String> keys = map.keySet(); // map에 저장된 키값만 빼옴
				
				for (String key : keys) {
					// 빼온 모든 키값과 value 값들중에 동일한 id와 pw가 있으면 로그인 성공
					if ((getid.equals(key)) && pw == map.get(key)){
						JOptionPane.showMessageDialog(null, "로그인 성공!",
								"로그인",JOptionPane.DEFAULT_OPTION); 
						new Client().ToServer(); // 클라이언트 채팅 시작
					}
					// 빼온 모든 키값과 value 값들중에 id와 pw가 동일하지 않으면 로그인 실패
					if ((getid.equals(key)) && pw != map.get(key) || (!getid.equals(key)) && pw != map.get(key)) {
						JOptionPane.showMessageDialog(null, "로그인 실패", "로그인",
								 JOptionPane.ERROR_MESSAGE); 
						return; 
					}		
				}	
			}
		});

	}

	public static Connection makeConnection() {
		String url = "jdbc:mysql://localhost:3306/login_db?chracterEncoding = UTF-8 & serverTimezone=UTC";

		String id = "root";	// 데이터베이스의 id
		String password = "1q2w3e4r!"; // 데이터베이스 pw
		Connection con = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // JABC 드라이버를 적재
			System.out.println("드라이버 적재 성공");
			con = DriverManager.getConnection(url, id, password); // 데이터베이스 연결
			System.out.println("데이터베이스 연결 성공");
		} catch (ClassNotFoundException e) {	// 예외처리
			System.out.println("드라이버를 찾을 수 없습니다.");
		} catch (SQLException e) {				// 예외처리
			System.out.println("연결에 실패하였습니다.");
		}
		return con;
	}

	public static void main(String[] args) throws SQLException {
		new LoginClient(); // LoginClient 객체 생성
	}
}
