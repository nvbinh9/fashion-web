package com.example.demo.entity;

import com.example.demo.entity.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "productIds")
    private String productIds;

    @Column(name = "quantityIds")
    private String quantityIds;

    @Column(name = "price")
    private String price;

    @Column
    private Boolean active;

}
