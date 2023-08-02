package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.karelin.project.models.Employee;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.models.Role;
import ru.karelin.project.services.EmployeeCredentialsService;
import ru.karelin.project.utility.EmployeeCredentialsValidator;
import ru.karelin.project.utility.EmployeeValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final EmployeeCredentialsValidator empCredentialsValidator;
    private final EmployeeCredentialsService empCredentialsService;
    private final EmployeeValidator employeeValidator;

    @Autowired
    public AdminController(EmployeeCredentialsValidator empCredentialsValidator, EmployeeCredentialsService empCredentialsService, EmployeeValidator employeeValidator) {
        this.empCredentialsValidator = empCredentialsValidator;
        this.empCredentialsService = empCredentialsService;
        this.employeeValidator = employeeValidator;
    }

    @GetMapping("")
    public String adminPage(){
        return "admin/index";
    }

    @GetMapping("/register")
    public String registrationPage(
            Model model,
            @ModelAttribute("employee") Employee employee) {
        return "admin/registration_employee";
    }

    @PostMapping("/register")
    public String performRegistration(
            Model model,
            @ModelAttribute("username") String username,
            @ModelAttribute("password") String password,
            @ModelAttribute("role") String role,
            @ModelAttribute("employee") @Valid Employee employee,
            BindingResult bindingResult
    )
    {
        System.out.println(bindingResult.getClass().toString());
        EmployeeCredentials employeeCredentials = new EmployeeCredentials(username, password, Role.valueOf(role));
        BindingResult errorsCredentials = empCredentialsValidator.validateAndGetBindingResult(employeeCredentials);
        employeeValidator.validate(employee, bindingResult);

        if(bindingResult.hasErrors() || errorsCredentials.hasErrors()){
            model.addAttribute("errorsCredentials", errorsCredentials);
            return "admin/registration_employee";
        }

        empCredentialsService.registerNewEmployee(employeeCredentials, employee);

        return "redirect:/admin";
    }
}