package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print(">>");
		String data = scanner.nextLine();
		try {
			InetAddress[] inetAddresses = InetAddress.getAllByName(data);
			
			for(InetAddress inetAddress : inetAddresses) {
				System.out.println(inetAddress.getHostAddress());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
