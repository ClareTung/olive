package com.olive.sftp.controller;

import com.olive.sftp.util.ZipUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongtangqiang
 */
@RestController
public class FileDownloadController {

    @RequestMapping(value = "zipDownload", method = RequestMethod.GET)
    public String zipDownload(HttpServletRequest request, HttpServletResponse response) {
        List<File> files = new ArrayList<>();
        files.add(new File("/Users/clare/Documents/demo1.xlsx"));
        files.add(new File("/Users/clare/Documents/demo2.xlsx"));
        // ZipUtil.makeZip(response, "demo.zip", files);
        return "download success";
    }

}
