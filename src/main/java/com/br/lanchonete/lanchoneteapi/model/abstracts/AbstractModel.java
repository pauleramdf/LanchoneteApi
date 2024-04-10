package com.br.lanchonete.lanchoneteapi.model.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractModel {
    @JsonIgnore
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @JsonIgnore
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @JsonIgnore
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdBy = "admin"; // ContextApplication.getCurrentUser().getLogin();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.updatedBy = this.createdBy;

    }

    @PreUpdate
    public void preUpdate() {
        this.updatedBy = "admin"; // ContextApplication.getCurrentUser().getLogin();
        this.updatedAt = LocalDateTime.now();
    }
}
