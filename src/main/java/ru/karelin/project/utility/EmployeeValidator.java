package ru.karelin.project.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.karelin.project.models.Employee;
import ru.karelin.project.services.EmployeeService;

import java.util.Optional;

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

        Optional<Employee> employeeFoundByEmail = employeeService.findByEmail(employee.getEmail());
        Optional<Employee> employeeFoundByPhoneNumber = employeeService.findByPhoneNumber(employee.getPhoneNumber());

       if(employee.getId() == 0){//is target is a new employee?(id == default value)
           if(employeeFoundByEmail.isPresent()){
               errors.rejectValue("email","", "This email is already taken!");
           }
           if(employeeFoundByPhoneNumber.isPresent()){
               errors.rejectValue("phoneNumber","", "This phone number is already taken!");
           }
       }
       else{//or it came from edit method?(id != default value)
           if(employeeFoundByEmail.get().getId() != employee.getId()){
               errors.rejectValue("email","", "This email is already taken!");
           }
           if(employeeFoundByPhoneNumber.get().getId() != employee.getId()){
               errors.rejectValue("phoneNumber","", "This phone number is already taken!");
           }
       }
    }
}
