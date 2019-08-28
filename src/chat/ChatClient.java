package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private static final String SERVER_IP = "192.168.56.1";
	private static final int SERVER_PORT = 7000;
	
	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;
		try {
			// 1. Scanner 생성(표준입력, 키보드 연결)
			scanner = new Scanner(System.in);
			
			// 2. 소켓생성
			socket = new Socket();
			
			// 3. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			
			// 4. I/O Stream 생성하기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); // true -> flush옵션 버퍼가 채워질때마다 출력하기.
	
			System.out.println("닉네임을 입력하세요.");
			String nickname = scanner.nextLine();
			pw.println("join:" + nickname);
			pw.flush();
			
			new ChatClientThread(socket, br).start();
			
			// 5. 키보드 입력 받기
			while(true) {
				System.out.print(">>");
				String line = scanner.nextLine();
				// quit입력시 처리
				if("quit".equals(line)) {
					log("closed by server2");
					pw.println("quit:");
					break;
				}
				
				// 공백처리
				if("".equals(line)) {
					line = " ";
				}
				// 6. 데이터 쓰기(송신)
				pw.println("message:" + line);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}
	
	private static void log(String log) {
		System.out.println("[Chat Client] " + log);
	}
}
