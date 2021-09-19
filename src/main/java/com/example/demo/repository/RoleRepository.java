package com.example.demo.repository;

import com.example.demo.entity.role.Role;
import com.example.demo.entity.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleName name);
}
