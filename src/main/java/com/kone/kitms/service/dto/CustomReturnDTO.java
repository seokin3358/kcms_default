package com.kone.kitms.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * KITMS 커스텀 응답 DTO 클래스
 * 
 * 이 클래스는 KITMS 시스템의 모든 API 응답을 표준화하기 위한 DTO입니다.
 * 일관된 응답 형식을 제공하여 클라이언트와의 통신을 용이하게 합니다.
 * 
 * 주요 필드:
 * - message: 응답 메시지
 * - status: HTTP 상태 코드
 * - data: 응답 데이터 (Map 형태)
 * - desc: 상세 설명
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class CustomReturnDTO {

    private String message;
    private String userMessage;
    private HttpStatus status;
    private int statusCode;
    private String desc;

    private Map<String, Object> data = new HashMap<String, Object>();

    public CustomReturnDTO() {}

    public CustomReturnDTO(String message, HttpStatus status, String desc) {
        this.message = message;
        this.setStatus(status);
        this.desc = desc;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void addColumn(String key, Object value) {
        data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
        this.statusCode = status.value();
    }
}
