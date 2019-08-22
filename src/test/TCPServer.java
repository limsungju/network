package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

// cmd -> d: -> D:\itcen\eclips-workspace\network\bin>java test.TCPServer 실행
public class TCPServer {
	private static final int PORT = 5000;
	public static void main(String[] args) {
		// 1. 서버소켓 생성
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket();
			
			// 2. Binding: Socket에 SocketAddress(IPAddress + Port) 바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost(); // Inet Address 얻어오기.
			//String localhostAddress = inetAddress.getHostAddress(); // Inet Address에서 IP Address 얻어오기.
			
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			//InetSocketAddress inetSocketAddress = new InetSocketAddress(localhostAddress, PORT); // 위에 코드랑 둘중 편한것 선택
			serverSocket.bind(inetSocketAddress);
			System.out.println("[TCPServer] binding " + inetAddress.getHostAddress() + ":" + PORT);
			
			
			// 3. accept: 클라이언트로 부터 연결요청(Connect)을 기다린다.
			Socket socket = serverSocket.accept(); // Blocking
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress(); // 클라이언트의 Inet Address 얻어오기
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress(); // 클라이언트의 IP Address 얻어오기
			int remoteHostPort = inetRemoteSocketAddress.getPort(); // 클라이언트의 PORT 얻어오기
			
			System.out.println("[TCPServer] connected from client [" + remoteHostAddress + ":" + remoteHostPort + "]");
			
			try {
				// 4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true) {
					// 5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); // Blocking
					if(readByteCount == -1) {
						// 정상종료: remote socket이 close() 메소드를 통해서 정상적으로 소켓을 닫은 경우
						System.out.println("[TCPServer] closed by client");
						break;
					}
					
					String data = new String(buffer, 0, readByteCount, "UTF-8"); // 읽은 수만큼 decoding 작업
					System.out.println("[TCPServer] received: " + data);
					
					// 6. 데이터 쓰기
					os.write(data.getBytes("UTF-8")); // 읽은 수만큼 encoding 작업
					
				}
				
			} catch(SocketException e) {
				System.out.println("[TCPServer] abnormal closed by client");
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
}
