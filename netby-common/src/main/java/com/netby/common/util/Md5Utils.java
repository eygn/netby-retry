package com.netby.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
@Slf4j
public class Md5Utils {


    public static String getMd5(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return StringUtil.getHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMd5(String str) {
        return getMd5(str.getBytes());
    }

    public static String getMd5(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        int numRead;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ((numRead = is.read(buffer)) > 0) {
                md.update(buffer, 0, numRead);
            }
            return StringUtil.getHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return null;
    }

    public static String getMd5(File file) throws IOException {
        return getMd5(Files.newInputStream(file.toPath()));
    }
}
