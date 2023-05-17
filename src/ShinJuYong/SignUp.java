package ShinJuYong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class SignUp extends JFrame { // 회원가입을 구현한 클래스입니다.
    int windowwidth = 400; // 창의 가로와 세로를 지정할 변수입니다. 변동사항 발생시 즉각 수정을 위해
    int windowheight = 400; // 변수를 따로 생성하여 설정해주었습니다.
    int widt = 240, heig = 30; // 컴포넌트의 가로와 세로를 지정할 변수입니다.
    int xcoor = (windowwidth - widt) / 2 - 10, ycoor = windowheight / 2 - 100; // 창 사이즈에 맞게끔 조정되도록 좌표를 설정해줍니다.
    int SubmitButtonwidth = 100; // 버튼의 가로 크기입니다.

    String verifyID, verifyPW, verifyEmail, verifyPhone; // 아래의 DB비교에서 사용할 문자열입니다.

    PreparedStatement pstmt = null; // 쿼리를 전송하기 위한 preparedstatement 객체를 필드에 선언해줍니다.

    public Color Yel = new Color(247, 230, 0); // 채팅 프로그램의 테마 색상이 될 Yellow 색상입니다. 공통적으로 사용됩니다.

    public SignUp() { // 회원가입 창의 생성자입니다.
        Connection con = Login.makeConnection(); // Login의 makeConnection함수를 가져와 객체를 생성ㅎ고
        try {
            String sql = "INSERT INTO user_table VALUES(?,?,?,?)"; // 쿼리 기본 틀을 작성합니다.
            pstmt = con.prepareStatement(sql); // 쿼리를 수행합니다.
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFrame SignupFrame = new JFrame("회원가입"); // 회원가입 JFrame입니다.
        SignupFrame.setSize(windowwidth, windowheight); // 창의 크기는 위에서 설정한대로 설정하고
        SignupFrame.setLayout(null); // setBounds를 사용하기 위해 layout은 null로 설정해줍니다.

        SignupFrame.getContentPane().setBackground(Yel); // 창의 빈 부분을 모두 테마 컬러로 설정합니다.

        JTextField getID = new JTextField("  사용할 아이디를 입력하세요"); // ID를 입력할 텍스트필드입니다.
        getID.setBounds(xcoor, ycoor, widt, heig); // 위에서 지정한 좌표에 지정한 크기로 컴포넌트를 위치시킵니다.
        getID.addMouseListener(new MouseAdapter() { // 텍스트필드 클릭시에 처리해줄 이벤트를 지정합니다.
            @Override
            public void mouseClicked(MouseEvent e){
                getID.setText(""); // 클릭시 기본 설정되어있던 문구가 사라지고
                getID.setForeground(Color.black); // 색상이 검정색으로 변경됩니다.
            }
        });

        ycoor += 40; // 위의 컴포넌트보다 40만큼 아래로 y좌표를 지정해줍니다.
        JPasswordField getPW = new JPasswordField("  사용할 비밀번호를 입력하세요");
        getPW.setBounds(xcoor, ycoor, widt, heig); // 이후로 동일한 부분들은 설명을 생략하도록 하겠습니다.
        getPW.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getPW.setText("");
                getPW.setForeground(Color.black);
            }
        });

        ycoor += 40;

        JTextField getEM = new JTextField("  사용할 이메일을 입력하세요");
        getEM.setBounds(xcoor, ycoor, widt, heig);
        getEM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getEM.setText("");
                getEM.setForeground(Color.black);
            }
        });

        ycoor += 40;

        JTextField getPN = new JTextField("  휴대전화 번호를 입력하세요");
        getPN.setBounds(xcoor, ycoor, widt, heig);
        getPN.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getPN.setText("");
                getPN.setForeground(Color.black);
            }
        });
        getPN.addKeyListener(new KeyAdapter() { // 휴대전화 번호 입력 텍스트필드에서 수행할 키 리스너를 정의합니다.
            @Override
            public void keyPressed(KeyEvent e) { // 키가 눌렸을 때
                if(e.getKeyCode() == KeyEvent.VK_ENTER){ // 키코드를 받고, 그것이 엔터키라면
                    verifyID = getID.getText(); // 앞서 TextField에 작성한 정보들을 모두 문자열로 가져옵니다.
                    verifyPW = getPW.getText();
                    verifyEmail = getEM.getText();
                    verifyPhone = getPN.getText();

                    try {
                        if(verifyID.equals("") || verifyID.equals("  사용할 아이디를 입력하세요")) { // 작성한 문자가 없거나, 기본 문구라면
                            JOptionPane.showMessageDialog(null, "잘못된 ID 입력입니다. 다시 입력해주세요"); // 경고창을 출력합니다.
                        } // 경고창은 JOptionPane을 사용해 구현해주었습니다.
                        else if(verifyPW.equals("") || verifyPW.equals("  사용할 비밀번호를 입력하세요")) {
                            JOptionPane.showMessageDialog(null, "잘못된 비밀번호 입력입니다. 다시 입력해주세요");
                        }
                        else if(verifyEmail.equals("") || verifyEmail.equals("  사용할 이메일을 입력하세요")) {
                            JOptionPane.showMessageDialog(null, "잘못된 이메일 입력입니다. 다시 입력해주세요");
                        }
                        else if(verifyPhone.equals("") || verifyPhone.equals("  사용할 전화번호를 입력하세요")) {
                            JOptionPane.showMessageDialog(null, "잘못된 전화번호 입력입니다. 다시 입력해주세요");
                        }
                        else { // 작성된 문자가 있고, 기본 문구가 아니라면
                            pstmt.setString(1, verifyID); // 앞서 생성한 쿼리의 1번쨰 열에 ID, 2번째 열에 PW, 동일하게 3,4열 모두 삽입하고
                            pstmt.setString(2, verifyPW);
                            pstmt.setString(3, verifyEmail);
                            pstmt.setString(4, verifyPhone);

                            int result = pstmt.executeUpdate(); // excuteUpdate메서드를 사용하여 DB를 업데이트해줍니다.

                            JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다. 로그인해주세요"); // 앞선 작업들을 수행 후 안내문구를 출력합니다.
                            dispose(); // 회원가입이 완료된 후 창을 닫습니다.
                            SignupFrame.setVisible(false);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        ycoor += 40;

        JButton SubmitButton = new JButton("회원 가입"); // 창 하단의 회원가입 버튼입니다.
        SubmitButton.addMouseListener(new MouseAdapter() { // 회원가입 버튼이 클릭되었을 시에 발생할 이벤트를 처리해줍니다.
            @Override
            public void mouseClicked(MouseEvent e){ // 위의 Enter Key Listener와 완전히 동일한 로직입니다.
                verifyID = getID.getText();
                verifyPW = getPW.getText();
                verifyEmail = getEM.getText();
                verifyPhone = getPN.getText();

                try {
                    if(verifyID.equals("") || verifyID.equals("  사용할 아이디를 입력하세요")) {
                        JOptionPane.showMessageDialog(null, "잘못된 ID 입력입니다. 다시 입력해주세요");
                    }
                    else if(verifyPW.equals("") || verifyPW.equals("  사용할 비밀번호를 입력하세요")) {
                        JOptionPane.showMessageDialog(null, "잘못된 비밀번호 입력입니다. 다시 입력해주세요");
                    }
                    else if(verifyEmail.equals("") || verifyEmail.equals("  사용할 이메일을 입력하세요")) {
                        JOptionPane.showMessageDialog(null, "잘못된 이메일 입력입니다. 다시 입력해주세요");
                    }
                    else if(verifyPhone.equals("") || verifyPhone.equals("  사용할 전화번호를 입력하세요")) {
                        JOptionPane.showMessageDialog(null, "잘못된 전화번호 입력입니다. 다시 입력해주세요");
                    }
                    else {
                        pstmt.setString(1, verifyID);
                        pstmt.setString(2, verifyPW);
                        pstmt.setString(3, verifyEmail);
                        pstmt.setString(4, verifyPhone);

                        int result = pstmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다. 로그인해주세요");
                        dispose();
                        SignupFrame.setVisible(false);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        SubmitButton.setBounds((windowwidth - SubmitButtonwidth) / 2, ycoor, SubmitButtonwidth, 30);

        getID.setForeground(Color.gray); // 작성할 텍스트필드, 버튼 내의 문자들의 초기값을 회색으로 설정해줍니다.
        getPW.setForeground(Color.gray);
        getEM.setForeground(Color.gray);
        getPN.setForeground(Color.gray);
        SubmitButton.setForeground(Color.gray);

        SignupFrame.add(getID); // JFrame에 모두 add해줍니다.
        SignupFrame.add(getPW);
        SignupFrame.add(getEM);
        SignupFrame.add(getPN);
        SignupFrame.add(SubmitButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈를 가져오는 메서드입니다.
        SignupFrame.setLocation((screenSize.width - windowwidth)/2,(screenSize.height - windowheight)/2); // 위의 사이즈에 좌측의 연산을 적용하여 모니터 중앙에 창이 위치할 수 있도록 합니다.

        SignupFrame.setVisible(true); // JFrame이 숨겨지지 않도록 하고
        SignupFrame.setResizable(false); // 사이즈를 수정할 수 없도록 합니다.
    }
}
