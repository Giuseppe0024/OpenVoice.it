package it.aulab.final_project_giuseppe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.final_project_giuseppe.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
