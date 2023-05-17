package ShinJuYong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FindPw extends JFrame { // 비밀번호 찾기 클래스는 회원가입 클래스와 거의 비슷한 과정으로 진행됩니다. 다른 부분만 주석으로 설명하도록 하겠습니다.
    int windowwidth = 400;
    int windowheight = 400;
    int widt = 240, heig = 30;
    int xcoor = (windowwidth - widt) / 2 - 10, ycoor = windowheight / 2 - 100;
    int SubmitButtonwidth = 120;

    String findID, findEM, findPN, verifyID, verifyEM, verifyPN;

    ResultSet rs;
    Statement stmt;

    public Color Yel = new Color(247, 230, 0);

    public FindPw() {
        FindMainFrame();
    }

    public void FindMainFrame() {
        Connection con = Login.makeConnection();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user_table");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFrame FindPwFrame = new JFrame("비밀번호 찾기");
        FindPwFrame.setSize(windowwidth, windowheight);
        FindPwFrame.setLayout(null);

        FindPwFrame.getContentPane().setBackground(Yel);

        JTextField getID = new JTextField("  등록된 아이디를 입력하세요");
        getID.setBounds(xcoor, ycoor, widt, heig);
        getID.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getID.setText("");
                getID.setForeground(Color.black);
            }
        });

        ycoor += 40;

        JTextField getEM = new JTextField("  등록된 이메일을 입력하세요");
        getEM.setBounds(xcoor, ycoor, widt, heig);
        getEM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getEM.setText("");
                getEM.setForeground(Color.black);
            }
        });

        ycoor += 40;

        JTextField getPN = new JTextField("  등록된 휴대전화 번호를 입력하세요");
        getPN.setBounds(xcoor, ycoor, widt, heig);
        getPN.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                getPN.setText("");
                getPN.setForeground(Color.black);
            }
        });
        getPN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){ // 마지막 전화번호 텍스트필드에서 엔터키를 입력하면
                    findID = getID.getText();
                    findEM = getEM.getText();
                    findPN = getPN.getText(); // 사용자가 입력한 값을 가져와 저장하고

                    try {
                        int check = 1;
                        while(rs.next()) { // DB에서 다음 행이 나오지 않을 때까지
                            verifyID = rs.getString("ID"); // ID를 가져오고
                            if(findID.equals(verifyID)) { // 사용자가 입력한 값과 동일하다면
                                verifyEM = rs.getString("EMail"); // 이메일 정보를 가져오고
                                if(findEM.equals(verifyEM)) { // 이메일도 동일하다면
                                    verifyPN = rs.getString("Phone"); // 전화번호를 가져옵니다.
                                    if(findPN.equals(verifyPN)) { // 위의 3개가 모두 동일하다면
                                        JOptionPane.showMessageDialog(null, "비밀번호는 "+rs.getString("PW")+" 입니다. 다시 로그인하세요"); // 비밀번호를 가져와 출력합니다.
                                        check = 0; // check를 0으로 지정하고
                                        FindPwFrame.setVisible(false); // 비밀번호 찾기 창을 닫습니다.
                                    }
                                }
                            }
                        }
                        if(check != 0) { // 위에서 check = 0을 지정하지 않는다면
                            JOptionPane.showMessageDialog(null, "등록되지 않은 계정입니다. 다시 시도해 주세요."); // 등록되지 않았다는 경고를 출력합니다.
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        ycoor += 40;

        JButton SubmitButton = new JButton("비밀번호 찾기");
        SubmitButton.addMouseListener(new MouseAdapter() { // 위의 EnterKeyListener와 동일합니다.
            @Override
            public void mouseClicked(MouseEvent e){

                findID = getID.getText();
                findEM = getEM.getText();
                findPN = getPN.getText();

                try {
                    int check = 1;
                    while(rs.next()) {
                        verifyID = rs.getString("ID");
                        if(findID.equals(verifyID)) {
                            verifyEM = rs.getString("EMail");
                            if(findEM.equals(verifyEM)) {
                                verifyPN = rs.getString("Phone");
                                if(findPN.equals(verifyPN)) {
                                    JOptionPane.showMessageDialog(null, "비밀번호는 "+rs.getString("PW")+" 입니다. 다시 로그인하세요");
                                    check = 0;
                                    FindPwFrame.setVisible(false);
                                }
                            }
                        }
                    }
                    if(check != 0) {
                        JOptionPane.showMessageDialog(null, "등록되지 않은 계정입니다. 다시 시도해 주세요.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }





            }
        });


        SubmitButton.setBounds((windowwidth - SubmitButtonwidth) / 2, ycoor, SubmitButtonwidth, 30);

        getID.setForeground(Color.gray);
        getEM.setForeground(Color.gray);
        getPN.setForeground(Color.gray);
        SubmitButton.setForeground(Color.gray);

        FindPwFrame.add(getID);
        FindPwFrame.add(getEM);
        FindPwFrame.add(getPN);
        FindPwFrame.add(SubmitButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
        FindPwFrame.setLocation((screenSize.width - windowwidth)/2,(screenSize.height - windowheight)/2); // 화면 중앙

        FindPwFrame.setVisible(true);
        FindPwFrame.setResizable(false);
    }

}
