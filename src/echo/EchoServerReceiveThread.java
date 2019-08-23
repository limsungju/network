package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress(); // 클라이언트의 Inet Address 얻어오기
		
		// 클라이언트의 IP Address 얻어오기, 클라이언트의 PORT 얻어오기
		EchoServer.log("connected from client [" + inetRemoteSocketAddress.getAddress().getHostAddress() + ":" + inetRemoteSocketAddress.getPort() + "]");
		
		try {
			// 4. I/O Stream 생성하기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); // true -> flush옵션 버퍼가 채워질때마다 출력하기.
			
			while(true) {
				// 5. 데이터 읽기(수신)
				String data = br.readLine();
				if(data == null) {
					// 정상종료: remote socket이 close() 메소드를 통해서 정상적으로 소켓을 닫은 경우
					EchoServer.log("closed by client");
					break;
				}
				EchoServer.log("received: " + data);
				
				// 6. 데이터 쓰기(송신)
				pw.println(data);
			}
			
		} catch(SocketException e) {
			EchoServer.log("abnormal closed by client");
		} catch(IOException e) {
			e.printStackTrace();
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
	
}
