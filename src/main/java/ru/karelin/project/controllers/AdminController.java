package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.karelin.project.models.Employee;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.models.Role;
import ru.karelin.project.services.EmployeeCredentialsService;
import ru.karelin.project.services.EmployeeService;
import ru.karelin.project.validators.EmployeeCredentialsValidator;
import ru.karelin.project.validators.EmployeeValidator;
import ru.karelin.project.utility.PageConfig;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final EmployeeCredentialsValidator empCredentialsValidator;
    private final EmployeeValidator employeeValidator;
    private final EmployeeCredentialsService empCredentialsService;
    private final EmployeeService employeeService;

    @Autowired
    public AdminController(EmployeeCredentialsValidator empCredentialsValidator, EmployeeCredentialsService empCredentialsService, EmployeeValidator employeeValidator, EmployeeService employeeService) {
        this.empCredentialsValidator = empCredentialsValidator;
        this.empCredentialsService = empCredentialsService;
        this.employeeValidator = employeeValidator;
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String employeesPage(Model model,
                                @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber
    ){
        Page<Employee> employeePage = employeeService.findAll(pageNumber-1, 10);

        if(!(employeePage.getTotalPages() <= 1)){
            PageConfig pageConfig = new PageConfig(pageNumber, employeePage.getTotalPages());
            model.addAttribute("pageConfig", pageConfig);
        }


        model.addAttribute("employees", employeePage.getContent());

        return "admin/index";
    }

    @GetMapping("/employees/{id}")
    public String employeePage(Model model,
                               @PathVariable("id") Integer id)
    {
        Optional<Employee> employeeOptional = employeeService.findById(id);

        if(employeeOptional.isEmpty()){
            return "errors/403";
        }

        model.addAttribute("employee", employeeOptional.get());
        model.addAttribute("role", employeeOptional.get().getCredentials().getRole().name().substring(5));

        return "admin/show";
    }

    @GetMapping("/employees/register")
    public String registrationPage(
            Model model,
            @ModelAttribute("employee") Employee employee) {

        model.addAttribute("roles", Role.values());
        return "admin/registration_employee";
    }

    @PostMapping("/employees/register")
    public String performRegistration(
            Model model,
            @ModelAttribute("username") String username,
            @ModelAttribute("password") String password,
            @ModelAttribute("role") String role,
            @ModelAttribute("employee") @Valid Employee employee,
            BindingResult bindingResult
    )
    {
        EmployeeCredentials employeeCredentials = new EmployeeCredentials(username, password, Role.valueOf(role));
        BindingResult errorsCredentials = empCredentialsValidator.validateNewAndGetBindingResult(employeeCredentials);
        employeeValidator.validate(employee, bindingResult);

        if(bindingResult.hasErrors() || errorsCredentials.hasErrors()){
            model.addAttribute("errorsCredentials", errorsCredentials);
            model.addAttribute("roles", Role.values());
            return "admin/registration_employee";
        }

        empCredentialsService.registerNewEmployee(employeeCredentials, employee);

        return "redirect:/admin/employees";
    }
}