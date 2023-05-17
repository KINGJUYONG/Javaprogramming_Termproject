package ShinJuYong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.JOptionPane;

public class Login extends JFrame {
    private int windowwidth = 400; // 생성될 창의 가로와 세로 크기를 미리 지정해줍니다.
    private int windowheight = 600;
    private int widt = 240, heig = 40; // 생성될 컴포넌트의 가로와 세로 폭입니다.
    private int xcoor = (windowwidth - widt) / 2 - 5, ycoor = windowheight / 2 + 20; // 컴포넌트가 위치할 좌표는 이렇게 상대적으로 지정해줍니다.
    private String LoginID, LoginPW, verifyID, verifyPW; // 로그인과 DB 확인을 위해 쓰일 문자열들입니다.
    ResultSet rs; // 결과물을 받을 ResultSet을 선언합니다.
    Statement stmt; // 쿼리 수행을 위해 필드에 Statement객체를 선언해줍니다.
    public static String myID; // 사용자의 ID가 저장될 문자열입니다.

    public Color Yel = new Color(247, 230, 0); // 이번 프로젝트의 테마 색상 Yel을 미리 선언해줍니다.

    public static Connection makeConnection() { // MYSQL DB 연동을 위해 makeConnection 메서드를 생성합니다.
        String url = "jdbc:mysql://localhost:3306/user_db?characterEncoding=UTF-8 & serverTimezone=UTC"; // MYSQL 연동을 위한 url을 지정합니다. 로컬호스트의 3306포트로 UTF-8 인코딩 된 데이터를 주고 받습니다.
        String user = "root"; // 계정명과 비밀번호를 입력합니다.
        String password = "kakashi0829";
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MYSQL JDBC 드라이버를 가져옵니다.
            System.out.println("Driver Loaded"); // 오류가 발생하지 않을 시, 드라이버가 로드되었다는 문구를 출력하고
            con = DriverManager.getConnection(url, user, password); // 위에서 지정한 url과 계정명, 비밀번호로 연결을 시도합니다.
            System.out.println("DB Connected"); // 성공하면 성공 문구를 출력합니다.
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public Login() { // 생성자입니다.

        JFrame LoginFrame = new JFrame("신주용톡"); // '신주용톡' JFrame을 생성합니다.
        LoginFrame.setUndecorated(true); // 제목표시줄을 보이지 않도록 합니다. UI의 디자인을 위해 제거해주었습니다.

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터의 사이즈를 가져와
        LoginFrame.setLocation((screenSize.width - windowwidth)/2,(screenSize.height - windowheight)/2); // 좌측의 연산을 통해 창을 화면 중앙에 위치하도록 합니다.

        JButton closeButton = new JButton("X"); // 제목표시줄이 없기에, 종료 버튼을 생성하기 위해 JButton을 사용하였습니다.
        closeButton.setBounds(365, -5, 45, 40); // 버튼의 위치와 크기를 지정하고
        closeButton.setBorderPainted(false); // 버튼 모양은 종료버튼과는 차이가 있기에 종료 버튼과의 유사성을 위해 테두리를 없앱니다.
        closeButton.setBackground(Yel); // 버튼의 배경색을 Yel로 지정합니다.
        closeButton.setForeground(Color.gray); // 버튼 내의 텍스트 색상을 gray로 지정합니다.

        closeButton.addMouseListener(new MouseAdapter() { // 버튼의 클릭 이벤트를 지정합니다.
            @Override
            public void mouseClicked(MouseEvent e) { // 클릭 이벤트가 발생했을 시에
                System.exit(0); // 프로그램을 종료합니다.
            }
        });

        JLabel LogoLabel = new JLabel(); // 로그인 화면 중앙의 로고를 생성하기 위해 JLabel을 사용합니다.
        ImageIcon Logo = new ImageIcon(("logo.jpg")); // ImageIcon을 이용해 이미지 파일을 가져오고
        LogoLabel.setIcon(Logo); // JLabel에 setIcon을 이용해 가져온 이미지 파일을 삽입해줍니다.
        LogoLabel.setBounds(97, 60, 200, 200); // 삽입한 이미지는 좌측의 좌표와 크기로 생성됩니다.

        LoginFrame.setSize(windowwidth, windowheight); // 창의 사이즈를 설정한 크기대로 지정해줍니다.
        LoginFrame.setLayout(null); // SetBounds를 사용하기 위해 배치 관리자는 null로 설정해줍니다.

        JTextField IDText = new JTextField("   계정"); // ID를 입력하는 텍스트필드입니다.
        IDText.setBounds(xcoor, ycoor, widt, heig); // 설정한 미리 지정한 좌표와 크기로 컴포넌트를 생성합니다.
        IDText.setForeground(Color.GRAY); // 글자색의 초기값은 GRAY이고
        IDText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){ // 클릭 이벤트가 발생할 시에는
                IDText.setText(""); // 기본 생성되는 문구를 삭제하고
                IDText.setForeground(Color.black); // 글자색을 검정으로 바꿉니다.
            }
        });

        ycoor += 37; // 위의 컴포넌트보다 37만큼 아래에 다음 컴포넌트를 생성합니다.

        JPasswordField PWText = new JPasswordField("   비밀번호"); // 위와 동일한 설정을 거칩니다.
        PWText.setBounds(xcoor, ycoor, widt, heig);
        PWText.setForeground(Color.GRAY);
        PWText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                PWText.setText("");
                PWText.setForeground(Color.black);
            }
        });

        ycoor += 50;

        JButton LoginButton = new JButton("  로그인");
        LoginButton.setBounds(xcoor, ycoor, widt, heig);
        LoginButton.setForeground(Color.gray);
        LoginButton.setFocusPainted(true);

        ycoor += 90;
        widt = 115; heig = 20;

        JButton SigninButton = new JButton("회원가입");
        SigninButton.setBounds(xcoor, ycoor, widt, heig);
        SigninButton.setForeground(Color.gray);

        xcoor += 125;

        JButton FindPWButton = new JButton("비밀번호 찾기");
        FindPWButton.setBounds(xcoor-8, ycoor, widt, heig);
        FindPWButton.setForeground(Color.gray);

        LoginButton.addMouseListener(new MouseAdapter() { // 로그인 버튼을 클릭하였을 때 발생하는 이벤트를 처리합니다.
            @Override
            public void mouseClicked(MouseEvent e){
                Connection con = makeConnection(); // Connection 객체 con에 makeconnection 메서드를 호출하고
                try {
                    stmt = con.createStatement(); // Statement를 생성합니다.
                    rs = stmt.executeQuery("SELECT * FROM user_table"); // user_table에서 모든 column을 가져오는 쿼리를 수행합니다.
                } catch (SQLException eve) {
                    eve.printStackTrace();
                }


                LoginID = IDText.getText(); // 사용자가 입력한 ID, PW를 받아서
                LoginPW = PWText.getText();

                try {
                    int check = 0; // ID, PW가 틀렸는지를 확인하는 변수입니다.
                    while(rs.next()) { // DB의 다음 행을 확인합니다.
                        System.out.println("Logging in");
                        verifyID = rs.getString("ID"); // DB에서 ID 열을 가져왔을때
                        if(LoginID.equals(verifyID)) { // 사용자가 입력한 값과 같다면
                            verifyPW = rs.getString("PW"); // PW 열도 가져와 확인합니다.
                            if(LoginPW.equals(verifyPW)) { // ID, PW 모두가 동일하다면
                                LoginFrame.setVisible(false); // 로그인 화면을 보이지 않게 하고
                                System.out.println("Log in Success"); // 로그인 성공 문구를 출력합니다.
                                check = 0; // check는 다시 0으로 초기화해줍니다.
                                myID = verifyID; // myID 스트링에 사용자가 입력한 값을 넣어주고
                                new Chat(); // Chat 클래스를 호출합니다.
                                break; // 이후 반복을 해제합니다.
                            }
                            else { // PW가 틀렸다면 check가 1
                                check = 1;
                            }
                        }
                        else { // ID가 틀렸고 비밀번호가 틀린 적 없다면 check가 2로 설정해주고
                            if(check != 1) {
                                check = 2;
                            }
                        }
                    }
                    if(check == 1) { // check가 1일때
                        JOptionPane.showMessageDialog(null, "비밀번호가 다릅니다. 다시 입력해주세요."); // 비밀번호 오류 문구를 출력하고
                        PWText.setText("   비밀번호"); // 비밀번호 TextField를 초기값으로 돌려줍니다.
                        PWText.setForeground(Color.gray); // 색상도 다시 gray로 하여 사용자로 하여금 입력해야할 값이 어디인지를 알려줍니다.
                    }
                    else if(check == 2) {
                        JOptionPane.showMessageDialog(null, "등록되지 않은 ID입니다. 다시 입력해주세요.");
                        IDText.setText("   계정");
                        PWText.setText("   비밀번호");
                        IDText.setForeground(Color.gray);
                        PWText.setForeground(Color.gray);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                FindPWButton.setForeground(Color.gray); // 버튼의 색상을 각각 변경해줍니다. 마지막에 클릭된 버튼만 black, 나머지는 gray입니다.
                SigninButton.setForeground(Color.gray);
                LoginButton.setForeground(Color.black);

            }
        });
        SigninButton.addMouseListener(new MouseAdapter() { // 회원가입 버튼을 누를 시에 발생하는 이벤트 처리입니다.
            @Override
            public void mouseClicked(MouseEvent e){
                FindPWButton.setForeground(Color.gray); // 마지막으로 눌린 버튼만 검은색, 나머지 버튼은 회색으로 변경합니다.
                SigninButton.setForeground(Color.black);
                LoginButton.setForeground(Color.gray);
                IDText.setForeground(Color.gray); // 글자색도 모두 gray로 설정하고
                PWText.setForeground(Color.gray);
                IDText.setText("   계정"); // 문구도 초기값으로 돌려줍니다.
                PWText.setText("   비밀번호");
                new SignUp(); // 이후 SignUp클래스를 호출하여 회원가입 창을 띄웁니다.
            }
        });
        FindPWButton.addMouseListener(new MouseAdapter() { // 위와 동일한 과정을 통해 비밀번호 찾기 창을 띄웁니다.
            @Override
            public void mouseClicked(MouseEvent e){
                FindPWButton.setForeground(Color.black);
                SigninButton.setForeground(Color.gray);
                LoginButton.setForeground(Color.gray);
                IDText.setForeground(Color.gray);
                PWText.setForeground(Color.gray);
                IDText.setText("   계정");
                PWText.setText("   비밀번호");
                new FindPw();
            }
        });

        LoginFrame.getContentPane().setBackground(Yel); // 로그인 창의 전체 색상을 지정한 테마색상으로 설정합니다.
        LoginButton.setBackground(Color.WHITE); // LoginButton의 배경색은 흰색
        SigninButton.setBackground(Yel); // 회원가입과 비밀번호 찾기의 배경색은 테마색으로 벼경하고
        FindPWButton.setBackground(Yel);
        SigninButton.setBorderPainted(false); // 버튼의 테두리를 없앱니다.
        FindPWButton.setBorderPainted(false);

        LoginFrame.add(IDText); // 모든 요소들을 Frame에 올려주고
        LoginFrame.add(PWText);
        LoginFrame.add(LoginButton);
        LoginFrame.add(SigninButton);
        LoginFrame.add(FindPWButton);
        LoginFrame.add(LogoLabel);
        LoginFrame.add(closeButton);

        LoginFrame.setVisible(true); // LoginFrame이 보이게 합니다.
        LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X버튼으로 종료시에 확실하게 프로세스를 종료할 수 있도록 설정합니다.
    }


    public static void main(String[] args) throws SQLException{
        Connection con = makeConnection(); // 위에서 수행한 것과 같이 MYSQL연결을 시도합니다.
        try {
            con.close(); // 작업이 모두 끝나고 나면 연결을 해제합니다.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Login();
    }

}
