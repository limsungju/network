package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClientBackUp {
	private static final String SERVER_IP = "192.168.1.3";
	private static final int SERVER_PORT = 8000;
	
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
			
			// 5. 키보드 입력 받기
			while(true) {
				System.out.print(">>");
				String line = scanner.nextLine();
				if("quit".equals(line)) {
					log("closed by server");
					break;
				}
				// 6. 데이터 쓰기(송신)
				pw.println(line);
				
				// 7. 데이터 읽기(수송)
				String data = br.readLine(); // Blocking
				if(data == null) {
					// 정상종료: remote socket이 close() 메소드를 통해서 정상적으로 소켓을 닫은 경우
					log("closed by server");
					break;
				}
				// 8. 콘솔출력
				System.out.println("<<" + data);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(scanner != null) {
					scanner.close();
				}
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void log(String log) {
		System.out.println("[Echo Client] " + log);
	}
}
