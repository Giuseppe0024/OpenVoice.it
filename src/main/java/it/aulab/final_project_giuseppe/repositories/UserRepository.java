package it.aulab.final_project_giuseppe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.aulab.final_project_giuseppe.models.User;

@Repository 
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
