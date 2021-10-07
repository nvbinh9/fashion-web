package com.example.demo.repository;

import com.example.demo.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    JwtToken findByToken (String jwt);
}
