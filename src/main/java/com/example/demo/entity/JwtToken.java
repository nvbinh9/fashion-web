package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "jwttoken")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    private Long userid;

    private String token;
    private Boolean active;

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
