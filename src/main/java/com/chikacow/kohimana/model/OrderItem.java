package com.chikacow.kohimana.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_order_item")
@Slf4j
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class OrderItem extends AbstractEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_per_unit", nullable = false)
    private BigDecimal pricePerUnit;

    @Column(name = "special_request", nullable = true)
    private String specialRequest;

}