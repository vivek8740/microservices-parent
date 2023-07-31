package com.springapp.prod.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    @PrePersist
    private void ensureId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

}
