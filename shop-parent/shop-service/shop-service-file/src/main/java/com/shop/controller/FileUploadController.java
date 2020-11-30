package com.shop.controller;


import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.file.FastDFSFile;
import com.shop.util.FastDFSUtil;
import org.csource.common.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class FileUploadController {

    @PostMapping
    public Result upload(@RequestParam("file")MultipartFile file) throws IOException, MyException {


        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(),
                file.getBytes(),
                StringUtils.getFilenameExtension(file.getOriginalFilename()));

        String[] ackInfo = FastDFSUtil.uplaod(fastDFSFile);

        // get access url for FastDFS
        String url = String.format("%s/%s/%s", FastDFSUtil.getTrackerInfo() , ackInfo[0], ackInfo[1]);

        return new Result(true, StatusCode.OK, "Upload successfully.", url);
    }
}
