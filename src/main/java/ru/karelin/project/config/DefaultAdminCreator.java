package ru.karelin.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import ru.karelin.project.models.Employee;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.models.Role;
import ru.karelin.project.repositories.EmployeeCredentialsRepository;
import ru.karelin.project.services.EmployeeCredentialsService;
import ru.karelin.project.services.EmployeeService;

@Configuration
public class DefaultAdminCreator {
    private EmployeeCredentialsService employeeCredentialsService;
    private EmployeeService employeeService;

    @Autowired
    public DefaultAdminCreator(EmployeeCredentialsService employeeCredentialsService, EmployeeService employeeService) {
        this.employeeCredentialsService = employeeCredentialsService;
        this.employeeService = employeeService;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void createDefaultAdmin () {
        Employee admin = this.getAdmin();
        EmployeeCredentials adminCred = this.getAdminCredentials();

        if(this.employeeService.existsByEmail(admin.getEmail())) {
            return;
        }

        adminCred.setRole(Role.ROLE_ADMIN);
        this.employeeCredentialsService.registerNewEmployee(adminCred, admin);
    }

    private boolean isAdminExists() {
        return employeeService.findByEmail("karelinvlad.dev@gmail.com")
                .isPresent();
    }

    @Bean
    @ConfigurationProperties(prefix = "users.default-admin.info")
    public Employee getAdmin() {
        return new Employee();
    }

    @Bean
    @ConfigurationProperties(prefix = "users.default-admin.credentials")
    public EmployeeCredentials getAdminCredentials() {
        return new EmployeeCredentials();
    }
}
