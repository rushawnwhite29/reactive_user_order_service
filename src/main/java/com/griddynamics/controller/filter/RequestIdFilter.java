package com.griddynamics.controller.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.UUID;

@Component
public class RequestIdFilter extends CommonsRequestLoggingFilter {

    private static final String REQUEST_ID_HEADER = "requestId";

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/orders");
    }

    @Override
    protected void beforeRequest(@NotNull HttpServletRequest request, @NotNull String message) {
        final String requestID = StringUtils.substringAfterLast(UUID.randomUUID().toString(), "-");

        request.setAttribute(REQUEST_ID_HEADER, requestID);
        ThreadContext.put(REQUEST_ID_HEADER, requestID);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, @NotNull String message) {
        request.removeAttribute(REQUEST_ID_HEADER);
        ThreadContext.remove(REQUEST_ID_HEADER);
    }

}
