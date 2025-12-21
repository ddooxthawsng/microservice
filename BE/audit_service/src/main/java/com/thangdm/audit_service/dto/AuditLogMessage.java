package com.thangdm.audit_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLogMessage {
    String serviceName;
    String userId;
    String username;
    String action;
    String details;
    String ipAddress;
    String status;
}
