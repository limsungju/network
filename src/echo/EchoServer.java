package echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
			while(true) {
				Socket socket = serverSocket.accept(); // Blocking
//				Thread thread = new EchoServerReceiveThread(socket);
//				thread.start();
				new EchoServerReceiveThread(socket).start();
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
	
	public static void log(String log) {
		System.out.println("[Echo Server#" + Thread.currentThread().getId() + "]" + log);
	}
	
}
