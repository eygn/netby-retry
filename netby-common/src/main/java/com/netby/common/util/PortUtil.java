package com.netby.common.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.BitSet;

/**
 * @author: byg
 */
public class PortUtil {

    private static final int MAX_PORT = 65535;
    private static final BitSet USED_PORT;

    static {
        USED_PORT = new BitSet(65536);
    }

    /**
     * 得到安全的端口
     *
     * @param port 默认端口
     * @return 端口
     */
    public static synchronized int getAvailablePort(int port) {
        if (port < 1) {
            return 1;
        } else {
            for (int i = port; i < MAX_PORT; ++i) {
                if (!USED_PORT.get(i)) {
                    try {
                        ServerSocket ignored = new ServerSocket(i);

                        try {
                            USED_PORT.set(i);
                            port = i;
                        } catch (Throwable var6) {
                            try {
                                ignored.close();
                            } catch (Throwable var5) {
                                var6.addSuppressed(var5);
                            }

                            throw var6;
                        }

                        ignored.close();
                        break;
                    } catch (IOException ignore) {
                    }
                }
            }

            return port;
        }
    }
}
