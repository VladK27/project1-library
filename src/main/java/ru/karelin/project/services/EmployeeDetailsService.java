package ru.karelin.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.repositories.EmployeeCredentialsRepository;
import ru.karelin.project.security.EmployeeDetails;

import java.util.Optional;

@Service
public class EmployeeDetailsService implements UserDetailsService {
    private final EmployeeCredentialsRepository employeeCredentialsRepository;

    @Autowired
    public EmployeeDetailsService(EmployeeCredentialsRepository employeeCredentialsRepository) {
        this.employeeCredentialsRepository = employeeCredentialsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EmployeeCredentials> credentialsOptional =
                employeeCredentialsRepository.findByUsername(username);

        if(credentialsOptional.isEmpty()){
            throw new UsernameNotFoundException("User with with this username not found!");
        }

        return new EmployeeDetails(credentialsOptional.get());
    }

}

