package com.example.demo.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadService {

    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String ip;

    public String avatarUpload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename(); // 获取源文件名称
        String uuid = IdUtil.fastSimpleUUID(); // 定义文件的唯一标识（前缀）
        String rootFilePath  = System.getProperty("user.dir") + "/demo/src/main/resources/static/avatar/" + uuid + "_" + originalFileName; // 获取文件上传的根路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath); // 把文件写入到上传的路径
        return "http://" + ip + "/api/download/avatar/" + uuid; // 返回结果URL
    }

    public String coverUpload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename(); // 获取源文件名称
        String uuid = IdUtil.fastSimpleUUID(); // 定义文件的唯一标识（前缀）
        String rootFilePath  = System.getProperty("user.dir") + "/demo/src/main/resources/static/cover/" + uuid + "_" + originalFileName; // 获取文件上传的根路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath); // 把文件写入到上传的路径
        return "http://" + ip + "/api/download/cover/" + uuid; // 返回结果URL
    }

    public JSON editorUpload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename(); // 获取源文件名称
        String uuid = IdUtil.fastSimpleUUID(); // 定义文件的唯一标识（前缀）
        String rootFilePath  = System.getProperty("user.dir") + "/demo/src/main/resources/static/editor/" + uuid + "_" + originalFileName; // 获取文件上传的根路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath); // 把文件写入到上传的路径
        String url = "http://" + ip + "/api/download/editor/" + uuid;
        JSONObject json = new JSONObject();
        json.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        json.set("data", arr);
        return json; // 返回结果URL
    }

}
