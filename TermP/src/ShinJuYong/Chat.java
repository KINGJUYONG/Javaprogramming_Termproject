package ShinJuYong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Chat { // 채팅창을 구현한 클래스입니다. 클라이언트의 역할을 합니다.
    int windowwidth = 400;
    int windowheight = 600;
    String msg, SendbyMe;

    private DataInputStream in;
    private DataOutputStream out;
    JTextArea TextBox;
    JTextArea logins;

    public Color Yel = new Color(247, 230, 0); // 사용할 색상들입니다.
    public Color Blu = new Color(165, 197, 222);
    public Color DarkBlu = new Color(155, 187, 212);

    public void connect() { // 서버와 연결되는 역할을 할 메서드입니다.
        try {
            Socket socket = new Socket("127.0.0.1", 7777); // 127.0.0.1(로컬호스트 IP)의 7777포트로 소켓을 사용합니다.
            System.out.println("Server Connected");

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            out.writeUTF(Login.myID); // 서버와 연결된 후 서버에게 전송한 메시지가 ID이므로 서버단에서 받을 때 해당 메시지를 클라이언트의 아이디로 인식합니다.
            while (in == null) { // 받아들이는 메시지가 더 이상 없을 때까지 반복하여
                msg = in.readUTF(); // 받아들인 메시지를 UTF로 읽어와 msg에 저장합니다.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chat() {
        logins = new JTextArea(""); // logins는 화면 상단의 접속자 리스트가 표시될 부분입니다. JTextArea를 사용하였습니다.
        connect(); // connect 메서드를 호출합니다.

        Receive receive = new Receive(); // 리시버를 개별적인 스레드로 수행시킵니다.
        receive.start();

        JFrame ChatFrame = new JFrame(); // 채팅창 전체에서 사용하는 Frame입니다.

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈를 받아오고
        ChatFrame.setLocation((screenSize.width - windowwidth)/2,(screenSize.height - windowheight)/2); // 화면 중앙에 창을 띄워줍니다.

        TextBox = new JTextArea();
        JTextArea Send = new JTextArea();
        JButton SendButton = new JButton("전송");

        SendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){ // 전송 버튼을 눌렀을 때
                SendbyMe = Send.getText(); // 사용자가 입력한 텍스트를 가져와
                try {
                    out.writeUTF(Login.myID + " : " + SendbyMe + "\n"); // ID : 메시지 \n 의 형태로 UTF를 적용하여 메시지를 전송합니다.
                } catch (IOException exec) {
                    exec.printStackTrace();
                }
                Send.setText(""); // 보낸 후에는 다시 Text를 초기화해줍니다.
            }
        });
        Send.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){ // 사용자가 입력하던 Text Area에서 엔터키를 눌렀을 때도 동일하게 작동합니다.
                    SendbyMe = Send.getText();
                    try {
                        out.writeUTF(Login.myID + " : " + SendbyMe + "\n");
                    } catch (IOException exec) {
                        exec.printStackTrace();
                    }
                    Send.setText("");
                }
            }
        });

        logins.setBounds(0, 0, 385, 80); // 컴포넌트들의 위치, 크기, 색상, 폰트 등을 모두 지정한 부분입니다.
        TextBox.setBounds(12, 88, 385, 350);
        TextBox.setBackground(Blu);
        Send.setBounds(0, 461, 284, 100);
        SendButton.setBounds(284, 461, 100, 100);
        SendButton.setBorderPainted(false);
        SendButton.setBackground(Yel);
        logins.setBackground(DarkBlu);
        logins.setForeground(Color.GRAY);
        Font bold = new Font("배달의민족 도현",Font.PLAIN, 12);
        Font TBFont = new Font("배달의민족 주아",Font.PLAIN, 17);
        logins.setFont(bold);
        TextBox.setFont(TBFont);

        ChatFrame.addWindowListener(new WindowAdapter() { // 창이 띄워졌을 떄, 사용자가 입력할 Text Area에 Focus를 설정해줍니다.
            public void windowOpened(WindowEvent e) {
                Send.requestFocus();
            }
        });
        ChatFrame.setLayout(null);
        ChatFrame.setSize(windowwidth, windowheight);
        ChatFrame.getContentPane().setBackground(Blu);

        ChatFrame.add(logins);
        ChatFrame.add(Send);
        ChatFrame.add(TextBox);
        ChatFrame.add(SendButton);

        ChatFrame.setResizable(false);
        ChatFrame.setVisible(true);
    }

    class Receive extends Thread {
        public void run() {
            try {
                while(true) {
                    msg = in.readUTF(); // 들어오는 메시지를 UTF로 읽어 저장하고
                    if(msg.contains("접!속!목록")) { // 메시지에 원하는 코드가 존재한다면
                        logins.setText(""); // 창 상단의 현재 접속자 명단을 지웁니다.
                        String msgtmp = msg.replaceAll("접!속!목록", " "); // 접속자 명단 코드를 지워주고 새 문자열에 저장합니다.
                        logins.setText("\n\n  OnLine : " + msgtmp); // 창 상단의 접속자 명단을 들어온 메시지를 토대로 업데이트 해줍니다.
                        if (true) {continue;}
                    }
                    else {
                        TextBox.append(msg); // 읽어온 메시지가 접속자 명단이 아닐 경우, 대화창에 출력해줍니다.
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Chat();
    }
}
