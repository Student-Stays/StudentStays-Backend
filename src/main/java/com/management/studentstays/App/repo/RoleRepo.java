package com.management.studentstays.App.repo;

import com.management.studentstays.App.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
  boolean existsByName(String roleName);
}
