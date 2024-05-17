package com.ds.darknesschat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static com.ds.darknesschat.Constants.MAX_PORT_VALUE;

public class Test {
    public static void main(String[] args) throws IOException {
        int endRange = 255;

        for (int i = 1; i < endRange; i++) {
            for (int j = 1; j < endRange; j++) {
                for (int k = 1; k < endRange; k++) {
                    for (int l = 0; l < endRange; l++) {
                        for (int m = 0; m < MAX_PORT_VALUE; m++) {
                            String ipAddress = i + "." + j + "." + k + "." + l;
                            Socket socket = new Socket(ipAddress, m);

                            if (socket.getInetAddress().isReachable(1000)) { // Проверяем доступность сервера
                                System.out.println("Server found: " + ipAddress);
                            }
                        }

                    }
                }
            }
        }
    }

}
