package ru.karelin.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.karelin.project.models.EmployeeCredentials;

import java.util.Optional;

@Repository
public interface EmployeeCredentialsRepository extends JpaRepository<EmployeeCredentials, Integer> {
    Optional<EmployeeCredentials> findByUsername(String username);
}
