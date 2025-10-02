package com.kone.kitms.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * KITMS 이미지 경로 관리 서비스 클래스
 * 
 * 이 클래스는 KITMS 시스템의 이미지 파일 경로 관리를 위한 서비스입니다:
 * - 이미지 파일 경로 동적 관리
 * - 폴더 구조 변경에 대한 유연한 대응
 * - 특정 파일 매핑 및 기본 경로 설정
 * - 리소스 존재 여부 확인
 * - 이미지 경로 자동 검색 및 반환
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
@ConfigurationProperties(prefix = "image-paths")
public class ImagePathService {

    private List<String> defaultPaths;
    private Map<String, String> fileMappings;

    /**
     * 파일명으로 전체 경로 찾기
     */
    public String findImagePath(String fileName) {
        
        // 1. 특정 파일 매핑에서 찾기
        if (fileMappings != null && fileMappings.containsKey(fileName)) {
            String mappedPath = fileMappings.get(fileName) + fileName;
            return mappedPath;
        }
        
        // 2. temp 폴더가 포함된 파일명인 경우 외부 images 폴더에서 찾기
        if (fileName.contains("/") && fileName.contains("temp_")) {
            // 외부 images 폴더에서 찾기
            String externalPath = "images/" + fileName;
            if (isExternalFileExists(externalPath)) {
                return externalPath;
            }
            // static/images에서도 찾기
            String staticPath = "static/images/" + fileName;
            if (isResourceExists(staticPath)) {
                return staticPath;
            } 
        }
        
        // 3. 기본 경로들에서 찾기
        if (defaultPaths != null) {
            for (String path : defaultPaths) {
                String fullPath = path + fileName;
                if (isResourceExists(fullPath)) {
                    return fullPath;
                }
            }
        }
        
        // 4. 외부 images 폴더에서 찾기 (일반 파일들)
        String externalPath = "images/" + fileName;
        if (isExternalFileExists(externalPath)) {
            return externalPath;
        } else {
            System.out.println("❌ 일반 외부 파일 없음: " + externalPath);
        }
        
        // 5. 기본 경로 반환
        String defaultPath = "static/images/" + fileName;
        return defaultPath;
    }
    
    /**
     * 리소스 존재 여부 확인 (classpath 내부)
     */
    private boolean isResourceExists(String path) {
        try {
            return getClass().getClassLoader().getResource(path) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 외부 파일 존재 여부 확인 (프로젝트 루트 기준)
     */
    private boolean isExternalFileExists(String path) {
        try {
            File file = new File(path);
            boolean exists = file.exists();
            boolean isFile = file.isFile();
            return exists && isFile;
        } catch (Exception e) {
            System.out.println("❌ 외부 파일 확인 오류: " + path + " | " + e.getMessage());
            return false;
        }
    }
    
    // Getter/Setter
    public List<String> getDefaultPaths() {
        return defaultPaths;
    }
    
    public void setDefaultPaths(List<String> defaultPaths) {
        this.defaultPaths = defaultPaths;
    }
    
    public Map<String, String> getFileMappings() {
        return fileMappings;
    }
    
    public void setFileMappings(Map<String, String> fileMappings) {
        this.fileMappings = fileMappings;
    }
}
