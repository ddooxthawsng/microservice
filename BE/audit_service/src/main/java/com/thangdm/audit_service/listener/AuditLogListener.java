package com.thangdm.audit_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thangdm.audit_service.dto.AuditLogMessage;
import com.thangdm.audit_service.entity.AuditLog;
import com.thangdm.audit_service.repository.AuditLogRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuditLogListener {
    
    AuditLogRepository auditLogRepository;
    ObjectMapper objectMapper;

    @KafkaListener(topics = "audit_logs", groupId = "audit-log-group")
    public void listen(String message) {
        try {
            log.info("Received audit log: {}", message);
            AuditLogMessage logMessage = objectMapper.readValue(message, AuditLogMessage.class);
            
            AuditLog auditLog = AuditLog.builder()
                    .serviceName(logMessage.getServiceName())
                    .userId(logMessage.getUserId())
                    .username(logMessage.getUsername())
                    .action(logMessage.getAction())
                    .details(logMessage.getDetails())
                    .ipAddress(logMessage.getIpAddress())
                    .status(logMessage.getStatus())
                    .build();
            
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error processing audit log message", e);
        }
    }
}
