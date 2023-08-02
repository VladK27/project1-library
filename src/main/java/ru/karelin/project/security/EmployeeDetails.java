package ru.karelin.project.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.karelin.project.models.EmployeeCredentials;

import java.util.Collection;
import java.util.Collections;

public class EmployeeDetails implements UserDetails {

    private final EmployeeCredentials employeeCredentials;

    public EmployeeDetails(EmployeeCredentials employeeCredentials) {
        this.employeeCredentials = employeeCredentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(
                employeeCredentials.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return this.employeeCredentials.getPassword();
    }

    @Override
    public String getUsername() {
        return this.employeeCredentials.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
