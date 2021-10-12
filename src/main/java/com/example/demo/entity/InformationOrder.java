package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String productName;

    private String productId;

    private String productPrice;

    private Long quantity;

    @ManyToOne(cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
    @JoinColumn(name = "orders_id")
    private Order order;
}
