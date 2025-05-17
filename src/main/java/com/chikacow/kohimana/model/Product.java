package com.chikacow.kohimana.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_product")
@Slf4j
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Product extends AbstractEntity<Long> {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    //@OneToOne(cascade = CascadeType.REMOVE)
    private String imageUrl;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "cate_id", referencedColumnName = "id")
    private Category category;

    @PrePersist
    private void prePersist() {

        this.code = this.code.trim().replaceAll("\\s+", "");;
        this.name = this.name.trim().replaceAll("\\s+", " ");;
        this.description = this.description.trim().replaceAll("\\s+", " ");;

    }

    @PreUpdate
    private void preUpdate() {
        this.code = this.code.trim().replaceAll("\\s+", "");;
        this.name = this.name.trim().replaceAll("\\s+", " ");;
        this.description = this.description.trim().replaceAll("\\s+", " ");;

    }

}
