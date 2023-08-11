package ru.karelin.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.Employee;
import ru.karelin.project.repositories.EmployeeRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> findByEmail(String email){
        return employeeRepository.findByEmail(email);
    }

    public Optional<Employee> findByPhoneNumber(String phoneNumber){
        return employeeRepository.findByPhoneNumber(phoneNumber);
    }

    public Page<Employee> findAll(Integer pageNumber, Integer itemsPerPage){
        return employeeRepository.findAll(PageRequest.of(pageNumber, itemsPerPage));
    }

    public Optional<Employee> findById(Integer id){
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }


}
