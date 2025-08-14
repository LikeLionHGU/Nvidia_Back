package com.likelionhgu.nvidia.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class S3ControllerTest {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // S3 버킷 이름

    private final S3Service s3Service;

    @PostMapping(value = "/poster", consumes = "multipart/form-data")
    public ResponseEntity<?> createShow(@RequestPart("poster") MultipartFile multipartFile){
        String uploadUrl = null;
        try {
            uploadUrl = s3Service.uploadFiles(multipartFile, "test/");
        } catch (IOException e) {
            return ResponseEntity.ok(Collections.singletonMap("error","이미지 업로드에 실패했습니다: " + e.getMessage()));
        }
        return ResponseEntity.ok().body(Map.of("uploadUrl",uploadUrl));
    }
}

