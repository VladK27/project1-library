package ru.karelin.project.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.services.EmployeeCredentialsService;

import java.util.Set;

@Component
public class EmployeeCredentialsValidator implements Validator {
    private final EmployeeCredentialsService employeeCredentialsService;

    @Autowired
    public EmployeeCredentialsValidator(EmployeeCredentialsService employeeCredentialsService) {
        this.employeeCredentialsService = employeeCredentialsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EmployeeCredentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmployeeCredentials credentials = (EmployeeCredentials) target;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        jakarta.validation.Validator validator = validatorFactory.getValidator();
        validatorFactory.close();

        Set<ConstraintViolation<EmployeeCredentials>> violations = validator.validate(credentials);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<EmployeeCredentials> violation : violations) {
                String propertyName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                errors.rejectValue(propertyName, "", errorMessage);
            }
        }

        if(employeeCredentialsService.findByUsername(credentials.getUsername()).isPresent()){
            errors.rejectValue("username", "", "This username already taken!");
        }
    }

    public BindingResult validateUpdatedAndGetBindingResult(EmployeeCredentials credentials){
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(credentials, "credentials");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        jakarta.validation.Validator validator = validatorFactory.getValidator();
        validatorFactory.close();

        Set<ConstraintViolation<EmployeeCredentials>> violations = validator.validate(credentials);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<EmployeeCredentials> violation : violations) {
                String propertyName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                errors.addError(new FieldError("", propertyName, errorMessage));
            }
        }

        return errors;
    }

    public BindingResult validateNewAndGetBindingResult(EmployeeCredentials credentials){
        BeanPropertyBindingResult errors = (BeanPropertyBindingResult) validateUpdatedAndGetBindingResult(credentials);

        if(employeeCredentialsService.findByUsername(credentials.getUsername()).isPresent()){
            errors.rejectValue("username", "", "This username already taken!");
        }

        return errors;
    }

}
