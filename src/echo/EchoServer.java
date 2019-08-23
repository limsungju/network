package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	private static final int PORT = 8000;
	
	public static void main(String[] args) {
		//1. 서버소켓 생성
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket();
			
			// 2. Binding : Socket에 SocketAddress(IP Address + Port) 바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost(); // Inet Address 얻어오기.
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			log("binding " + inetAddress.getHostAddress() + ":" + PORT);
			
			// 3. accept: 클라이언트로 부터 연결요청(Connect)을 기다린다.
			Socket socket = serverSocket.accept(); // Blocking
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress(); // 클라이언트의 Inet Address 얻어오기
			
			// 클라이언트의 IP Address 얻어오기, 클라이언트의 PORT 얻어오기
			log("connected from client [" + inetRemoteSocketAddress.getAddress().getHostAddress() + ":" + inetRemoteSocketAddress.getPort() + "]");
			
			try {
				// 4. I/O Stream 생성하기
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); // true -> flush옵션 버퍼가 채워질때마다 출력하기.
				
				while(true) {
					// 5. 데이터 읽기(수신)
					String data = br.readLine();
					if(data == null) {
						// 정상종료: remote socket이 close() 메소드를 통해서 정상적으로 소켓을 닫은 경우
						log("closed by client");
						break;
					}
					log("received: " + data);
					
					// 6. 데이터 쓰기(송신)
					pw.println(data);
				}
				
				
			} catch(SocketException e) {
				log("abnormal closed by client");
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				// 7. Socket 자원정리
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 8. Server Socket 자원정리
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void log(String log) {
		System.out.println("[Echo Server] " + log);
	}
	
}
