package com.chikacow.kohimana.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_product")
@RequiredArgsConstructor
@Getter
@Setter
public class Product extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cate_id", referencedColumnName = "id")
    private Category category;


}
