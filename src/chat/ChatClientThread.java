package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

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
