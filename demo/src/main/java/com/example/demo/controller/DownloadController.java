package com.example.demo.controller;

import com.example.demo.service.DownloadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/download")
public class DownloadController {

    @Resource
    DownloadService downloadService;

    @GetMapping("/{type}/{uuid}")
    public void download(HttpServletResponse response, @PathVariable String type, @PathVariable String uuid) {
        downloadService.download(response, type, uuid);
    }

}
