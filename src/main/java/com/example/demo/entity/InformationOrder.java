package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "infomation_order")
@Getter
@Setter
public class InformationOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    private String productId;

    private String productPrice;

    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
