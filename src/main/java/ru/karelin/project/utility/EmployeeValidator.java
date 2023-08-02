package ru.karelin.project.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.karelin.project.models.Employee;
import ru.karelin.project.services.EmployeeService;

@Component
public class EmployeeValidator implements Validator {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeValidator(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Employee.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employee employee = (Employee) target;

        if(employeeService.findByEmail(employee.getEmail())
                .isPresent()){
            errors.rejectValue("email", "", "This email already taken!");
        }
        if(employeeService.findByPhoneNumber(employee.getPhoneNumber())
                .isPresent()){
            errors.rejectValue("phoneNumber", "", "This phone number already taken!");
        }
    }
}
