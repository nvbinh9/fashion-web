package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(name = "slug")
    private String slug;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "category")
//    private List<Product> products = new ArrayList<>();
}
