package com.thangdm.audit_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String serviceName;
    String userId;
    String username;
    String action;
    
    @Column(columnDefinition = "TEXT")
    String details;
    
    String ipAddress;
    String status;

    @CreatedDate
    LocalDateTime timestamp;
}
