package com.bajaks.WebshopProductAPI.controllers;

import com.bajaks.WebshopProductAPI.dto.TestResponse;
import com.bajaks.WebshopProductAPI.services.MinioService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
//@AllArgsConstructor
@RequestMapping(path = "/")
@Slf4j
public class Test
{
    @Autowired
    private MinioService minioService;
    @GetMapping(path = "/test")
    public TestResponse test(){
        TestResponse res = TestResponse.builder().message("It works!").build();
        log.atDebug().log("The message sent is {}",res.getMessage());
        return res;
    }
    @PostMapping(path = "/upload")
    public TestResponse upload(@RequestParam("file")MultipartFile file){
        try {
            minioService.upload(file);
            log.atInfo().log("File {} successfully uploaded!",file.getOriginalFilename());
            return new TestResponse("Success");
        } catch (IOException | InsufficientDataException | ServerException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            log.atError().log("Error: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
