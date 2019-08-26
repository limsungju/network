package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {
	private static final String SERVER_IP = "192.168.1.3";
	
	public static void main(String[] args) {
		Scanner scanner = null;
		DatagramSocket socket = null;
		try {
			// 1. 키보드 연결
			scanner = new Scanner(System.in);
			
			// 2. 소켓 생성
			socket = new DatagramSocket(); // 클라이언트 UDP 소켓은 PORT가 필요없다.
			
			while(true) {
				// 3. 사용자 입력을 받음
				System.out.print(">>");
				String message = scanner.nextLine();
				
				if("quit".equals(message)) {
					break;
				}
				
				// 4. 메세지 전송
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, new InetSocketAddress(SERVER_IP, UDPEchoServer.PORT));
				socket.send(sendPacket);
				
				// 5. 메세지 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[UDPEchoServer.BUFFER_SIZE], UDPEchoServer.BUFFER_SIZE);
				socket.receive(receivePacket); // Blocking
				
				// 6. data 처리(확인)
				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				message = new String(data, 0, length, "UTF-8");
				System.out.println("<<" + message);
				
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
			if(socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
		
		
	}
}
