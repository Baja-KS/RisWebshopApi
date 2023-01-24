package com.bajaks.WebshopProductAPI.controllers;

import com.bajaks.WebshopProductAPI.dto.TestResponse;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/")
@Slf4j
public class Test
{
//    @Autowired
//    private MinioService minioService;
    @GetMapping(path = "/test")
    public TestResponse test(){
        TestResponse res = TestResponse.builder().message("It works!").build();
        log.atDebug().log("The message sent is {}",res.getMessage());
        return res;
    }
//    @PostMapping(path = "/upload")
//    public TestResponse upload(@RequestParam("file")MultipartFile file){
//        Path p = Path.of(file.getOriginalFilename());
//        try {
//            minioService.upload(p,file.getInputStream(),file.getContentType());
//            return new TestResponse("Success!");
//        } catch (MinioException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
