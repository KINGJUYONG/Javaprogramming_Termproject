import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server { // 서버 역할을 할 클래스입니다. 패키지 외부에 있고, 다른 클래스들과의 종속성이 없습니다.
    private Socket socket; // Socket을 지정할 객체와
    private String msg; // 메시지를 전달할 문자열을 선언해줍니다.
    public static ArrayList<String> loginlist = new ArrayList<String>(); // 로그인한 유저의 목록을 받아올 ArrayList를 생성해줍니다.

    public static Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>(); // String과 DataOutPutStream을 가지는 맵을 생성해줍니다.
    private final ArrayList<DataOutputStream> outs = new ArrayList<>(); // 클라이언트에게 보내줄 Output이 담긴 ArrayList를 생성해줍니다.

    public void makeThread() throws IOException { // setting 메서드를 생성합니다.
        ServerSocket serverSocket = new ServerSocket(7777); // 포트 7777로 통신하는 서버소켓을 생성합니다.
        while (true) { // 아래의 무한히 동작합니다.
            System.out.println("Waiting for connection"); // 서버가 작동중일때, 작동중이라는 문구를 출력하고
            socket = serverSocket.accept(); // serverSocket을 accept해줍니다. 클라이언트를 받아들이는 역할을 합니다.
            System.out.println(socket.getInetAddress() + " Connected"); // 연결되었다면, IP를 받아와 연결되었다는 문구를 출력합니다.
            Receiver receiver = new Receiver(socket); // 스레드 클래스 receiver에 위에서 받아온 socket을 소켓 정보로 하여 스레드를 생성합니다.
            receiver.start(); // 리시버 스레드를 시작합니다.
        }
    }

    public static void main(String[] args) throws IOException { // 메인함수입니다.
        Server server = new Server(); // 새로운 서버 객체를 생성하고
        server.makeThread(); // 서버의 makeThread 메서드를 호출합니다.
    }

    public void addClient(String ID, DataOutputStream out) throws IOException { // 클라이언트의 접속정보(여부)를 담당합니다.
        sendMessage("\n------------" + ID + " connected. ------------\n\n"); // 연결된 클라이언트의 ID를 다른 클라이언트들에게 전송합니다.
        clientsMap.put(ID, out); // 클라이언트맵에 ID와 out을 삽입하고
        outs.add(out); // ArrayList outs에도 삽입합니다.
        loginlist.add(ID); // 사용자 리스트에는 ID만을 삽입합니다.

        msg = ""; // msg는 다시 빈 값으로 초기화해준 다음
        for (int i = 0; i < loginlist.size(); i++) { // loginlist의 사이즈만큼을 반복하는 for 루프입니다. 접속목록을 메시지로 전달하기 위해 구현하였습니다.
            if(i < loginlist.size() - 1 ) { // 마지막의 전 전 인덱스까지는
                msg += "접!속!목록" + loginlist.get(i) + " / "; // 접!속!목록이라는 문자와 접속한 사용자의 ID를 더하고, 뒤에는 /를 붙여줍니다. 이를 msg에 이어붙입니다.
            } // 클라이언트가 받아 서버의 메시지인지 판별할 때, 혼선이 일어나지 않도록 하기 위해 접!속!목록 이라는 문자를 추가하여주었습니다.
            else { // 전 전 인덱스가 아닐때, 전 인덱스일 때
                msg += "접!속!목록" + loginlist.get(i); // 뒤의 /을 떼어내고 출력합니다.(깔끔한 전달을 위함)
            }
        }
        sendMessage(msg); // 접속 목록이 이어붙은 msg를 클라이언트에게 전송합니다.
    }

    public void removeClient(String ID) { // 클라이언트의 연결 해제를 담당하는 메서드입니다.
        sendMessage("\n------------" + ID + " disconnected. ------------\n\n");
        clientsMap.remove(ID); // 클라이언트 맵에서 사용자의 ID를 삭제하고
        for (int i = 0; i < loginlist.size(); i++) {
            if (loginlist.get(i) == ID) { // 접속이 종료된 사용자의 ID를 loginlist내에서 찾아내어 삭제합니다.
                loginlist.remove(i);
            }
        }

        msg = "";
        for (int i = 0; i < loginlist.size(); i++) { // 위의 메서드에서 접속목록을 전송한 것과 같은 로직을 사용하였습니다.
            if(i < loginlist.size() - 1 ) {
                msg += "접!속!목록" + loginlist.get(i) + " / ";
            }
            else {
                msg += "접!속!목록" + loginlist.get(i);
            }
        }
        sendMessage(msg);
    }

    public void sendMessage(String msg) { // 클라이언트에게 메시지를 전달하는 메서드입니다.
        for (DataOutputStream o : outs) { // out의 끝까지
            try {
                o.writeUTF(msg); //  msg를 UTF로 쓰고
                o.flush(); // 현재 버퍼에 저장된 내용을 클라이언트로 전송합니다. flush가 수행된 후에 버퍼는 비워집니다.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Receiver extends Thread { // 스레드 클래스입니다.
        private final DataInputStream in;
        private final DataOutputStream out;
        public String ID;

        public Receiver(Socket socket) throws IOException { // Receiver의 생성자입니다. Socket을 입력하면
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            ID = in.readUTF(); // 첫 input을 UTF로 읽어들여 ID로 저장합니다.
            addClient(ID, out); // 이것을 addClient 메서드를 이용해 새 클라이언트를 연결합닡다.
        }

        public void run() { // 개별 스레드로 동작할 부분입니다.
            try {
                while (in != null) { // 들어오는 정보가 없을 때까지 -> 들어오는 정보가 있으면 무한반복
                    msg = in.readUTF(); // UTF로 읽어들인 정보를 받아
                    sendMessage(msg); // 다시 클라이언트들에게 전송해줍니다.
                }
            } catch (IOException e) { // IOException 발생시
                removeClient(ID); // removeClient 메서드를 사용해 클라이언트들에게 접속 종료를 알립니다.
            }
        }
    }
}