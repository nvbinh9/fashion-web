package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(name = "slug")
    private String slug;

    @Column(name = "title")
    private String title;

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
