package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			byte[] ipAddresses = inetAddress.getAddress(); // 4byte
			
			
			System.out.println(hostName);
			System.out.println(hostAddress);
			for(byte ipAddress:ipAddresses) {
				System.out.print(ipAddress & 0x000000ff); // 부호비트 0으로 셋팅
				System.out.print(".");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
