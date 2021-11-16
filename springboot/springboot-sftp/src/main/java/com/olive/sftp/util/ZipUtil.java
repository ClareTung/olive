package com.olive.sftp.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author dongtangqiang
 */
@Slf4j
public class ZipUtil {

    /**
     * 当前系统的临时目录
     */
    // System.getProperty("java.io.tmpdir") 需要配置JVM启动参数
    private static final String FILE_PATH = "/Users/clare/Documents" + File.separator;

    private static final int ZIP_BUFFER_SIZE = 8192;

    public static String makeZip(/*HttpServletResponse response,*/String zipFileName, List<File> fileList) {
        // zip文件路径
        String zipPath = FILE_PATH + zipFileName;

        try {
            // 创建zip输出流
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath))) {
                byte[] buffer = new byte[1024];
                // 将文件放入zip包
                for (File file : fileList) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        // 循环读入需要下载的文件内容，打包到zip文件
                        while ((len = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        out.closeEntry();
                    }
                }
            }

            // downloadFile()
        } catch (Exception e) {
            log.error("制作zip包出错", e);
            e.printStackTrace();
        } finally {
            // zip文件删除
            // fileList.add(new File(zipPath));
            // deleteFile(fileList);
        }

        return zipPath;
    }

    /**
     * 删除文件
     *
     * @param fileList
     */
    private static void deleteFile(List<File> fileList) {
        for (File file : fileList) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 文件下载
     *
     * @param response
     * @param zipFileName
     */
    private static void downloadFile(HttpServletResponse response, String zipFileName) {
        try {
            String path = FILE_PATH + zipFileName;
            File file = new File(path);
            if (file.exists()) {
                try (InputStream ins = new FileInputStream(path);
                     BufferedInputStream bins = new BufferedInputStream(ins);
                     OutputStream outs = response.getOutputStream();
                     BufferedOutputStream bouts = new BufferedOutputStream(outs)) {
                    response.setContentType("application/x-download");
                    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
                    int bytesRead = 0;
                    byte[] buffer = new byte[ZIP_BUFFER_SIZE];
                    while ((bytesRead = bins.read(buffer, 0, ZIP_BUFFER_SIZE)) != -1) {
                        bouts.write(buffer, 0, bytesRead);
                    }
                    bouts.flush();
                }
            }
        } catch (Exception e) {
            log.error("文件下载出错", e);
        }
    }

}
