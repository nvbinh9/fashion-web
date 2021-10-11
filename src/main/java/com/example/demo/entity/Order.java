package com.example.demo.entity;

import com.example.demo.entity.audit.UserDateAudit;
import com.example.demo.entity.role.Role;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<InformationOrder> informationOrders;

    @Column(name = "price")
    private String price;

}
