package com.chikacow.kohimana.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price; // price not the moment the order is made

    @Column(name = "note")
    private String note;
}