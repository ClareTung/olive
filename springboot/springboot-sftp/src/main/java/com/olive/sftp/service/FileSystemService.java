package com.olive.sftp.service;

import java.io.File;
import java.io.InputStream;

/**
 * @author dongtangqiang
 */
public interface FileSystemService {

    boolean uploadFile(String targetPath, InputStream inputStream) throws Exception;

    boolean uploadFile(String targetPath, File file) throws Exception;

    File downloadFile(String targetPath) throws Exception;

    boolean deleteFile(String targetPath) throws Exception;
}
