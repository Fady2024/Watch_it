package com.example.fms_market.pages;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class Welcome {

    public static String getMacAddress() throws Exception {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X", mac[i]));
            if (i < mac.length - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public static void b(String expectedMacAddress) throws Exception {
        String macAddress = getMacAddress();
        if (!macAddress.equals(expectedMacAddress)) {
            throw new SecurityException("This application is not authorized to run on this device.");
        }
    }
}
