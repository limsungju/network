package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import echo.EchoServer;

public class ChatServerThread extends Thread {
	private Socket socket;
	private String nickname;
	private List<PrintWriter> listWriters;
	private PrintWriter pw = null;
	
	public ChatServerThread(Socket socket, List<PrintWriter> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}
	
	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress(); // 클라이언트의 Inet Address 얻어오기
		
		// 클라이언트의 IP Address 얻어오기, 클라이언트의 PORT 얻어오기
		EchoServer.log("connected from client [" + inetRemoteSocketAddress.getAddress().getHostAddress() + ":" + inetRemoteSocketAddress.getPort() + "]");
		
		try {
			// 4. I/O Stream 생성하기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); // true -> flush옵션 버퍼가 채워질때마다 출력하기.
			while(true) {
				// 5. 데이터 읽기(수신)
				String data = br.readLine();
				System.out.println(data);
				if(data == null) {
					// 정상종료: remote socket이 close() 메소드를 통해서 정상적으로 소켓을 닫은 경우
					EchoServer.log("closed by client");
					break;
				}
				
				// 프로토콜 분석
				String[] tokens = data.split(":");
				if("join".equals(tokens[0])) {
					doJoin(tokens[1], pw);
				} else if("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				} else if("quit".equals(tokens[0])) {
					doQuit(pw);
					break;
				} else {
					ChatServer.log("에러: 알수 없는 요청(" + tokens[0] + ")");
					continue;
				}
			}
			
		} catch(SocketException e) {
			ChatServer.log("abnormal closed by client");
		} catch(IOException e) {
			ChatServer.log(this.nickname + "님이 채팅방을 나갔습니다.");
		} finally {
			// 7. Socket 자원정리
			if(socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
		}
	}
	
	private void doJoin(String nickName, PrintWriter writer) {
		this.nickname = nickName;
		
		String data = nickname + "님이 참여하였습니다.";
		broadcast(data);
		
		addWriter(writer);
		
		pw.println("join:ok");
		pw.flush();
		
	}
	
	private void addWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}
	
	private void broadcast(String data) {
		synchronized (listWriters) {
			for(PrintWriter writer : listWriters) {
				PrintWriter printWriter = writer;
				printWriter.println(data);
				printWriter.flush();
			}
		}
	}
	
	private void doMessage(String message) {
		broadcast(this.nickname + ":" + message);
	}
	
	private void doQuit(PrintWriter writer) {
		removeWriter(writer);
		
		String data = nickname + "님이 퇴장 하였습니다.";
		broadcast(data);
	}
	
	private void removeWriter(PrintWriter writer) {
		synchronized (listWriters) {
			listWriters.remove(writer);
			
		}
	}
	
}