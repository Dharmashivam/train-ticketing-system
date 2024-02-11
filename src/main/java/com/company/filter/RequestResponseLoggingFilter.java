package com.company.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logRequest(request);
        filterChain.doFilter(request, response);
        logResponse(response);
    }

    private void logRequest(HttpServletRequest request) {
        logger.info("Request URL: " + request.getRequestURL().toString());
        logger.info("Request Method: " + request.getMethod());
        logger.info("Request Headers: " + getRequestHeaders(request));
        logger.info("Request Body: " + getRequestBody(request));
    }

    private void logResponse(HttpServletResponse response) {
        logger.info("Response Status: " + response.getStatus());
        logger.info("Response Headers: " + getResponseHeaders(response));
    }

    private String getRequestHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(header -> headers.append(header).append(": ").append(request.getHeader(header)).append("; "));
        return headers.toString();
    }

    private String getRequestBody(HttpServletRequest request) {
        // Instead of consuming the request body, just get its content type and length
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();

        // You can log other request parameters as needed
        return "Content Type: " + contentType + ", Content Length: " + contentLength;
    }

    private String getResponseHeaders(HttpServletResponse response) {
        StringBuilder headers = new StringBuilder();
        response.getHeaderNames().forEach(header -> headers.append(header).append(": ").append(response.getHeader(header)).append("; "));
        return headers.toString();
    }
}
