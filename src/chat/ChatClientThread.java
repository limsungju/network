package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import echo.EchoServer;

public class ChatClientThread extends Thread {
	private Socket socket = null;
	private BufferedReader br;

	public ChatClientThread(Socket socket, BufferedReader br) {
		this.socket = socket;
		this.br = br;
	}
	
	@Override
	public void run() {
		String data = null;
		try {
			while (true) {
				System.out.print(">>");
				data = br.readLine();
				// quit입력시 while문 탈출
				if(data==null) {
					break;
				}
				// 8. 콘솔출력
				System.out.println(data);
			}
		} catch (SocketException e) {
			// 서버에서 비정상 종료 되었을 때
			EchoServer.log("closed by server");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
