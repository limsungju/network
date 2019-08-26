package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	
	private static final String SERVER_IP = "192.168.1.3";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {
		Socket socket = null;
		try {
			// 1. 소켓생성
			socket = new Socket();
			
			// 1.1 socket buffer size 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();
			
			System.out.println(receiveBufferSize + ":" + sendBufferSize);
			
			// 1.2 socket buffer size 변경
			socket.setReceiveBufferSize(1024 * 10);
			socket.setSendBufferSize(1024 * 10);
			
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();
			
			System.out.println(receiveBufferSize + ":" + sendBufferSize);
			
			// 1.3 SO_SODELAY(Nagle Algorithm off) // Nagle Algorithm 이란 보낸것을 ACK 받는 것
			socket.setTcpNoDelay(true);
			
			// 1.4 SO_TIMEOUT
			socket.setSoTimeout(1000); // 단위 ms
			
			
			
			// 2. 서버연결
			InetSocketAddress inetSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(inetSocketAddress);
			System.out.println("[TCPClient] connected");
			
			// 3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			// 4. 데이터 쓰기
			String data = "안녕하세요~\n";
			os.write(data.getBytes("UTF-8"));
			
			// 5. 데이터 읽기
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer); // Blocking // Time Out이 걸리는 위치
			if(readByteCount == -1) {
				// 정상종료: remote socket이 close() 메소드를 통해서 정상적으로 소켓을 닫은 경우
				System.out.println("[TCPClient] closed by server");
				return;
			}
			
			data = new String(buffer, 0, readByteCount, "UTF-8");
			System.out.println("[TCPClient] received: " + data);
			
		} catch(SocketTimeoutException e) {
			System.out.println("{[TCP Client] time out");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
