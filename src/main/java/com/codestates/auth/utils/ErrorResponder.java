package com.codestates.auth.utils;

import com.codestates.response.ErrorResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// ErrorResponse를 출력 스트림으로 생성하는 클래스
public class ErrorResponder {
    public static void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.getWriter()
                .write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}
