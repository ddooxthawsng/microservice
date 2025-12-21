package com.thangdm.account_service.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thangdm.account_service.annotation.LogAudit;
import com.thangdm.account_service.dto.AuditLogMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @AfterReturning(pointcut = "@annotation(logAudit)", returning = "result")
    public void logSuccess(JoinPoint joinPoint, LogAudit logAudit, Object result) {
        sendAuditLog(joinPoint, logAudit, "SUCCESS", null);
    }

    @AfterThrowing(pointcut = "@annotation(logAudit)", throwing = "ex")
    public void logFailure(JoinPoint joinPoint, LogAudit logAudit, Throwable ex) {
        sendAuditLog(joinPoint, logAudit, "FAILURE", ex.getMessage());
    }

    private void sendAuditLog(JoinPoint joinPoint, LogAudit logAudit, String status, String errorDetails) {
        try {
            // Get Authentication safely
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = "anonymous";
            String username = "anonymous";

            if (authentication != null && authentication.isAuthenticated()) {
                username = authentication.getName();
                userId = username; 
            }

            // Get IP Address safely
            String ipAddress = "unknown";
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    ipAddress = request.getRemoteAddr();
                }
            } catch (Exception e) {
                log.warn("Could not retrieve IP address for audit log: {}", e.getMessage());
            }
            
            String details = logAudit.details();
            if (errorDetails != null) {
                details += " | Error: " + errorDetails;
            }

            AuditLogMessage message = AuditLogMessage.builder()
                    .serviceName("account-service")
                    .userId(userId)
                    .username(username)
                    .action(logAudit.action())
                    .details(details)
                    .ipAddress(ipAddress)
                    .status(status)
                    .build();

            String jsonMessage = objectMapper.writeValueAsString(message);
            
            // Send to Kafka asynchronously and catch any IMMEDIATE errors
            try {
                kafkaTemplate.send("audit_logs", jsonMessage);
                log.info("Audit log queued for Kafka: {}", logAudit.action());
            } catch (Exception kafkaEx) {
                log.error("CRITICAL: Kafka send failed - Audit logging skipped to prevent business logic failure. Error: {}", kafkaEx.getMessage());
            }

        } catch (Exception e) {
            log.error("Failed to process audit log metadata", e);
        }
    }
}
