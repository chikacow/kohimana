package com.chikacow.kohimana.model;


import com.chikacow.kohimana.util.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_category")
@RequiredArgsConstructor
@Getter
@Setter
public class Category extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @OneToMany(mappedBy = "category")
    private List<Product> productList = new ArrayList<>();

}
