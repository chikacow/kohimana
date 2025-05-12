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


    @ManyToOne
    @JoinColumn(name = "cate_id", referencedColumnName = "id")
    private Category category;


}
