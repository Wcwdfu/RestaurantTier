package com.site.restauranttier.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageService {

    private final Path uploadDir = Paths.get("postImage"); // 저장할 디렉토리 경로를 "postImage"로 설정

    public LocalStorageService() throws IOException {
        // 서비스 초기화 시 "uploads" 디렉토리가 없으면 생성
        Files.createDirectories(uploadDir);
    }

    public String storeImage(MultipartFile file) throws IOException {
        // 파일명에 UUID를 추가하여 중복을 방지
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path storagePath = uploadDir.resolve(fileName); // resolve 메소드를 사용하여 저장 경로 생성

        // 파일 저장
        Files.copy(file.getInputStream(), storagePath);

        // 저장된 파일의 접근 가능한 URL 또는 경로를 반환
        // 여기서는 파일 시스템의 경로를 반환합니다.
        // 웹 애플리케이션에서 파일에 접근해야 한다면, 이 경로를 웹 접근 가능한 URL로 변환해야 할 수 있습니다.
        return storagePath.toAbsolutePath().toString();
    }
}
