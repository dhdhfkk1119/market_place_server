package com.market.market_place._core._utils;

import com.market.market_place._core._config.UploadConfig;
import com.market.market_place._core._exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadUtil {
    private final UploadConfig uploadConfig;


    /*프로필 이미지 파일을 서버에 업로드 하는 메서드*/
    public String uploadProfileImage(String base64Image, String subDirName) throws IOException {
        // 어느 파일에 저장하지 파일 경로 설정
        String fullUploadPath = Paths.get(uploadConfig.getRootDir(), subDirName).toString();
        createUploadDirectory(fullUploadPath);

        // Base64 → byte[]
        byte[] bytes = Base64.getDecoder().decode(base64Image);

        // 파일 확장자 (예: png)
        String extension = ".png"; // WebP, JPG 등 필요에 따라 유동적으로 처리

        // 고유한 파일명 생성
        String uniqueFileName = generateUniqueFileName(extension);

        // 최종 저장 경로
        Path filePath = Paths.get(fullUploadPath, uniqueFileName);

        // 파일 저장
        Files.write(filePath, bytes);

        return uniqueFileName; // DB에는 URL 또는 파일명만 저장
    }


    private String generateUniqueFileName(String extension) {
        // 1. 현재 날짜와 시간을 "YYYYMMDD HHMMSS" 형태로 포맷팅
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDD_HHmmss"));
        // 2. UUID(범용 고유 식별자를 만들때 사용)
        String uuid = UUID.randomUUID().toString().substring(0,8);
        return timestamp +  "_" + uuid + extension;
    }

    // 파일 확장자만 추출 해주는 메서드
    private String getFileExtension(String originFilename) {
        if(originFilename == null || originFilename.lastIndexOf(".") == -1){
            return ""; // 확장자가 없으면 빈 문자열을 반환
        }
        // 마지막 점(.) 문자 이후 문자열을 확장자로 변환
        // profile.jpg --> latIndexOf(".") --> 7을 반환(확장자전 까지)
        return originFilename.substring(originFilename.lastIndexOf("."));
    }

    // 폴더를 생성하는 메서드
    private void createUploadDirectory(String fullUploadPath) throws IOException{
        Path uploadPath = Paths.get(fullUploadPath);

        // 디렉토리가 존재하지 않으면 생성
        // C:/uploads/profiles/ 경로에 파일이 없으면
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
    }

    public void deleteProfileImage(String imagePath,String subDirName){
        if(imagePath != null && imagePath.isEmpty() == false){
            try{
                // uploads/profiles/123123123.jpg
                // 1.단계 전체 경로에서 파일명만 추출
                String fileName = imagePath.substring(imagePath.lastIndexOf("/")+1);

                // 2.단계 실제 파일 시스템 경로 생성
                Path filePath = Paths.get(subDirName,fileName);

                // 3.단계 : 파일이 존재하면 삭제 , 없으면 아무것도 안함
                Files.deleteIfExists(filePath);

            }catch (IOException e){
                throw new Exception400("프로필 이미지를 삭제하지 못했습니다");
            }
        }
    }
}
