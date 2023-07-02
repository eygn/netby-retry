package com.netby.common.util.vo;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.netby.common.util.StringUtil;
import com.netby.common.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: byg
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RuntimeVO extends BaseVO {

    private StringBuilder message;
    private String errorMessage;
    private RandomAccessFile logFile;
    private Integer exitVal;
    private Long time;

    public RuntimeVO() {
        this.message = new StringBuilder();
    }

    public void setLogFilePath(String logFile) {
        if (StringUtil.isNotEmpty(logFile)) {
            try {
                Files.createParentDirs(new File(logFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.logFile = new RandomAccessFile(logFile, "rw");
            } catch (Exception e) {
                this.logFile = null;
            }
        }
    }

}
