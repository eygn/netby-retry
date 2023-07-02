package com.netby.common.util;

import com.netby.common.util.generator.IDGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author: byg
 */
@Slf4j
public class ZipUtils {
    /**
     * zip压缩
     *
     * @param children   子文件流
     * @param closeChild 是否关闭子文件流
     * @return 临时压缩文件
     * @throws IOException IOException
     */
    public static File zip(Map<String, InputStream> children, boolean closeChild) throws IOException {
        String path = System.getProperty("java.io.tmpdir");
        String zipFilename = path + IDGenerator.getUId() + ".zip";
        zip(Paths.get(zipFilename), children, closeChild);
        return new File(zipFilename);
    }

    public static void zip(Path zipPath, Map<String, InputStream> children, boolean closeChild) throws IOException {
        // 获取每一个子文件，并压缩成一个文件
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))){
            for (Map.Entry<String, InputStream> entry : children.entrySet()) {
                zos.putNextEntry(new ZipEntry(entry.getKey()));
                IoUtils.copy(entry.getValue(), zos);
                if (closeChild) {
                    entry.getValue().close();
                }
            }
        }
        log.info("压缩文件[{}]完成，文件数[{}].", zipPath, children.size());
    }
}
