package com.cognizant.ems.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Exercise 7 — Entity Auditing.
 *
 * MappedSuperclass shared by any entity that needs creation/modification
 * tracking. Employee and Department both extend this so every insert and
 * update automatically records who made the change and when, without
 * repeating the four audit fields in each entity.
 *
 * @EntityListeners(AuditingEntityListener.class) is what actually wires
 * Hibernate's lifecycle callbacks (prePersist/preUpdate) to Spring Data's
 * auditing infrastructure, which in turn calls the AuditorAware bean
 * (see config/AuditorAwareImpl.java) to resolve "who" made the change.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEntity {

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}
