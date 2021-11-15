package com.olive.sftp.service.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.olive.sftp.config.SftpProperties;
import com.olive.sftp.service.FileSystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author dongtangqiang
 */
@Slf4j
@Service
public class FileSystemServiceImpl implements FileSystemService {

    // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";

    @Resource
    private SftpProperties config;

    /**
     * 创建sftp连接
     *
     * @return
     * @throws Exception
     */
    private ChannelSftp createSftp() throws Exception {
        JSch jsch = new JSch();
        log.info("Try to connect sftp[" + config.getUserName() + "@" + config.getHost() + "], use password[" + config.getPassword()
                + "]");

        Session session = createSession(jsch, config.getHost(), config.getUserName(), config.getPort());
        session.setPassword(config.getPassword());
        session.connect(config.getSessionConnectTimeout());

        log.info("Session connected to {}.", config.getHost());

        Channel channel = session.openChannel(config.getProtocol());
        channel.connect(config.getChannelConnectedTimeout());

        log.info("Channel created to {}.", config.getHost());
        return (ChannelSftp) channel;
    }

    /**
     * 创建Session
     *
     * @param jsch
     * @param host
     * @param userName
     * @param port
     * @return
     */
    private Session createSession(JSch jsch, String host, String userName, Integer port) throws Exception {
        Session session = null;

        if (port <= 0) {
            session = jsch.getSession(userName, host);
        } else {
            session = jsch.getSession(userName, host, port);
        }

        if (session == null) {
            throw new Exception(host + " session is null");
        }

        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, config.getSessionStrictHostKeyChecking());
        return session;
    }

    /**
     * 加密秘钥方式登陆
     *
     * @return
     */
    private ChannelSftp connectByKey() throws Exception {
        JSch jsch = new JSch();

        // 设置秘钥和密码，支持秘钥方式登陆
        if (StringUtils.isNotBlank(config.getPrivateKey())) {
            if (StringUtils.isNotBlank(config.getPassphrase())) {
                // 设置带口令的秘钥
                jsch.addIdentity(config.getPrivateKey(), config.getPassphrase());
            } else {
                // 设置不带口令的秘钥
                jsch.addIdentity(config.getPrivateKey());
            }
        }

        log.info("Try to connect sftp[" + config.getUserName() + "@" + config.getHost() + "], use private key[" + config.getPrivateKey()
                + "] with passphrase[" + config.getPassphrase() + "]");

        Session session = createSession(jsch, config.getHost(), config.getUserName(), config.getPort());
        // 设置登陆超时时间
        session.connect(config.getSessionConnectTimeout());
        log.info("Session connected to " + config.getHost() + ".");

        // 创建sftp通信通道
        Channel channel = session.openChannel(config.getProtocol());
        channel.connect(config.getChannelConnectedTimeout());
        log.info("Channel created to " + config.getHost() + ".");
        return (ChannelSftp) channel;
    }

    /**
     * 创建一级或多级目录
     *
     * @param dirPath
     * @param sftp
     * @return
     */
    private boolean createDirs(String dirPath, ChannelSftp sftp) {
        if (dirPath != null && !dirPath.isEmpty() && sftp != null) {
            String[] dirs = Arrays.stream(dirPath.split("/"))
                    .filter(StringUtils::isNotBlank)
                    .toArray(String[]::new);

            for (String dir : dirs) {
                try {
                    sftp.cd(dir);
                    log.info("Change directory {}", dir);
                } catch (Exception e) {
                    try {
                        sftp.mkdir(dir);
                        log.info("Create directory {}", dir);
                    } catch (SftpException e1) {
                        log.error("Create directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                    }

                    try {
                        sftp.cd(dir);
                        log.info("Change directory {}", dir);
                    } catch (SftpException e1) {
                        log.error("Change directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 关闭连接
     *
     * @param sftp
     */
    private void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    log.info("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param targetPath
     * @param inputStream
     * @return
     * @throws Exception
     */
    @Override
    public boolean uploadFile(String targetPath, InputStream inputStream) throws Exception {
        ChannelSftp sftp = createSftp();

        try {
            sftp.cd(config.getRoot());
            log.info("Change path to {}", config.getRoot());

            int index = targetPath.lastIndexOf("/");
            String fileDir = targetPath.substring(0, index);
            String fileName = targetPath.substring(index + 1);

            boolean dirs = createDirs(fileDir, sftp);
            if (!dirs) {
                log.error("Remote path error. path:{}", targetPath);
                throw new Exception("Upload File failure");
            }

            sftp.put(inputStream, fileName);
            return true;
        } catch (Exception e) {
            log.error("Upload file failure. TargetPath: {}", targetPath, e);
            throw new Exception("Upload File failure");
        } finally {
            disconnect(sftp);
        }
    }

    /**
     * 上传文件
     *
     * @param targetPath
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public boolean uploadFile(String targetPath, File file) throws Exception {
        return uploadFile(targetPath, new FileInputStream(file));
    }

    /**
     * 下载文件
     *
     * @param targetPath
     * @return
     * @throws Exception
     */
    @Override
    public File downloadFile(String targetPath) throws Exception {
        ChannelSftp sftp = createSftp();
        OutputStream outputStream = null;

        try {
            sftp.cd(config.getRoot());
            log.info("Change path to {}", config.getRoot());

            File file = new File(targetPath.substring(targetPath.lastIndexOf("/") + 1));

            outputStream = new FileOutputStream(file);
            sftp.get(targetPath, outputStream);
            log.info("Download file success. TargetPath: {}", targetPath);
            return file;
        } catch (Exception e) {
            log.error("Download file failure. TargetPath: {}", targetPath, e);
            throw new Exception("Download File failure");
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            disconnect(sftp);
        }
    }

    /**
     * 删除文件
     *
     * @param targetPath
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteFile(String targetPath) throws Exception {
        ChannelSftp sftp = createSftp();

        try {
            sftp.cd(config.getRoot());
            sftp.rm(targetPath);
            return true;
        } catch (Exception e) {
            log.error("Delete file failure. TargetPath: {}", targetPath, e);
            throw new Exception("Delete File failure");
        } finally {
            disconnect(sftp);
        }
    }
}
