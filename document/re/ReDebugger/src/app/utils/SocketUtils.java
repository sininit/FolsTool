package app.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class SocketUtils {



    public static boolean isUsePort(int port) {
        try {
            ServerSocket socket = new ServerSocket();
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(port));
            socket.close();
            return false;
        } catch (Throwable var3) {
            return true;
        }
    }



    public static int findFreePort() {
        for(int i = 0; i < 5; ++i) {
            int suggestedPort = checkPortIsFree();
            if (suggestedPort != -1) {
                return suggestedPort;
            }
        }
        throw new RuntimeException("Unable to find a free port");
    }
    private static int checkPortIsFree() {
        try {
            ServerSocket socket = new ServerSocket();
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(0));
            int localPort = socket.getLocalPort();
            socket.close();
            return localPort;
        } catch (IOException var3) {
            return -1;
        }
    }
}
