package com.example.demo.controller;

import cn.hutool.json.JSON;
import com.example.demo.common.Result;
import com.example.demo.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    UploadService uploadService;

    @PostMapping("/avatar")
    public Result<?> avatarUpload(MultipartFile file) throws IOException {
        return Result.success(uploadService.avatarUpload(file));
    }

    @PostMapping("/cover")
    public Result<?> coverUpload(MultipartFile file) throws IOException {
        return Result.success(uploadService.coverUpload(file));
    }

    @PostMapping("/editor")
    public JSON editorUpload(MultipartFile file) throws IOException {
        return uploadService.editorUpload(file);
    }

}
