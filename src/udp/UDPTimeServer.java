package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPTimeServer {
	public static final int PORT = 9000;
	public static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		

		try {
			// 1. socket 생성
			socket = new DatagramSocket(PORT);

			while (true) {
				// 2. data 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket); // Blocking
				

				// 3. data 처리(확인)
				byte[] data = receivePacket.getData();
				int length = receivePacket.getLength();
				String message = new String(data, 0, length, "UTF-8");
				
				System.out.println("[UDP Time Server] received:" + message);
				
				

				// 4. data 전송
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),
						receivePacket.getPort()); // 보낼 메세지와 메세지의 길이를 받는 쪽의 주소와 포트에 넣어준다.
				socket.send(sendPacket);

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}

	}

}
