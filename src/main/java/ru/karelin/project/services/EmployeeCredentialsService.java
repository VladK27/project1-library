package ru.karelin.project.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.Employee;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.repositories.EmployeeCredentialsRepository;
import ru.karelin.project.repositories.EmployeeRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeCredentialsService {

    private final EmployeeCredentialsRepository employeeCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EmployeeCredentialsService(EmployeeCredentialsRepository employeeCredentialsRepository, PasswordEncoder passwordEncoder) {
        this.employeeCredentialsRepository = employeeCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<EmployeeCredentials> findByUsername(String username){
        return employeeCredentialsRepository.findByUsername(username);
    }

    @Transactional(readOnly = false)
    public EmployeeCredentials save(EmployeeCredentials employeeCredentials){
        String encodedPassword = passwordEncoder.encode(employeeCredentials.getPassword());
        employeeCredentials.setPassword(encodedPassword);

        return employeeCredentialsRepository.save(employeeCredentials);
    }

    @Transactional
    public void registerNewEmployee(EmployeeCredentials credentials, Employee employee){
        Session session = entityManager.unwrap(Session.class);

        credentials.setPassword(
                passwordEncoder.encode(
                        credentials.getPassword()
                ));

        session.persist(employee);
        session.persist(credentials);

        employee.setCredentials(credentials);
        credentials.setEmployee(employee);
    }

    public Optional<EmployeeCredentials> findByUsernameAndLoadEmployee(String username){
        Optional<EmployeeCredentials> credentialsOptional =
                employeeCredentialsRepository.findByUsername(username);

        if(credentialsOptional.isPresent()){
            Session session = entityManager.unwrap(Session.class);

            EmployeeCredentials employeeCredentials = credentialsOptional.get();
            session.persist(employeeCredentials);
            Employee employee = employeeCredentials.getEmployee();

            return Optional.of(employeeCredentials);
        }

        return credentialsOptional;
    }
}
