package com.chikacow.kohimana.model;


import com.chikacow.kohimana.util.enums.CategoryType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@Table(name = "tbl_category")
@Slf4j
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category extends AbstractEntity<Long> {

    @Column(name = "category_id", unique = true, nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> productList;

}
