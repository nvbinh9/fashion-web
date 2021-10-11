package com.example.demo.entity;

import com.example.demo.entity.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "name")
    private String name;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "shortdescription")
    private String shortDescription;

    @Column(name = "content")
    private String content;

    @Column(name = "price")
    private String price;

    @Column(name = "size")
    private String size;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
