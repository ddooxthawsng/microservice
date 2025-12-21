package com.thangdm.account_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
