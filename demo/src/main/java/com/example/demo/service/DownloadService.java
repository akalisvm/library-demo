package com.example.demo.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
public class DownloadService {

    public void download(HttpServletResponse response, String type, String uuid) {
        OutputStream outputStream; // 新建一个输出流对象
        String rootFileName = System.getProperty("user.dir") + "/demo/src/main/resources/static/" + type + "/"; // 获取文件上传的根路径
        List<String> fileNames = FileUtil.listFileNames(rootFileName); // 获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(uuid)).findAny().orElse(""); // 获取跟参数一致的文件
        try {
            if(StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(rootFileName + fileName); // 通过文件的路径获取文件字节流
                outputStream = response.getOutputStream(); // 通过输出流返回文件
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

}
