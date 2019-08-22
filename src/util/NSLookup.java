package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookup {

	public static void main(String[] args) {
		
		try {
			InetAddress[] inetAddresses = InetAddress.getAllByName("www.naver.com");
			
			for(InetAddress inetAddress : inetAddresses) {
				System.out.println(inetAddress.getHostAddress());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
