package org.example.ip;

import java.net.InetAddress;

public class InetAddressClass {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
