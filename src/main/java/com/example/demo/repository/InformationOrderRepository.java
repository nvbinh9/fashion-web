package com.example.demo.repository;

import com.example.demo.entity.InformationOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationOrderRepository extends JpaRepository< InformationOrder, Long> {
}
