package com.netby.common.util;

import com.netby.common.util.vo.RuntimeVO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * @author: byg
 */
@SuppressWarnings("unused")
public class RuntimeUtil {

    private static Process startProcess(ProcessBuilder pb, RuntimeVO vo) {
        try {
            return pb.start();
        } catch (IOException e) {
            vo.setErrorMessage(e.getMessage());
            return null;
        }
    }

    private static void getProcessOutput(Process p, RuntimeVO vo) {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (true) {
            try {
                String str = br.readLine();
                if (null == str) {
                    break;
                }
                vo.getMessage().append(str).append("\n");
                if (vo.getLogFile() != null) {
                    vo.getLogFile().seek(vo.getLogFile().length());
                    vo.getLogFile().write(str.getBytes(StandardCharsets.UTF_8));
                    vo.getLogFile().write("\n".getBytes());
                }
            } catch (IOException e) {
                vo.setErrorMessage(e.getMessage());
                return;
            }
        }
    }

    private static void waitForAndDestroyProcess(Process p, RuntimeVO vo) {
        while (true) {
            try {
//                int i =
                p.waitFor();
                vo.setExitVal(p.exitValue());
                p.destroy();
                return;
//                return i;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static RuntimeVO run(List<String> cmd, String workingDirectory, String logFile) {
        long time = System.currentTimeMillis();
        ProcessBuilder pb = new ProcessBuilder(cmd);
        if (workingDirectory != null) {
            pb.directory(new File(workingDirectory));
        }
        pb.redirectErrorStream(true);
        RuntimeVO vo = new RuntimeVO();
        if (StringUtil.isNotEmpty(logFile)) {
            vo.setLogFilePath(logFile);
        }
        Process p = startProcess(pb, vo);
        if (p != null) {
            getProcessOutput(p, vo);
            waitForAndDestroyProcess(p, vo);
        }
        vo.setTime(System.currentTimeMillis() - time);
        return vo;
    }
}
