package ru.karelin.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.security.EmployeeDetails;
import ru.karelin.project.services.EmployeeCredentialsService;
import ru.karelin.project.services.EmployeeService;

import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeCredentialsService employeeCredentialsService;

    @Autowired
    public EmployeeController(EmployeeCredentialsService employeeCredentialsService) {
        this.employeeCredentialsService = employeeCredentialsService;
    }

    @GetMapping("/profile")
    public String profilePage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

        Optional<EmployeeCredentials> credentialsOptional =
                employeeCredentialsService.findByUsernameAndLoadEmployee(employeeDetails.getUsername());

        if(credentialsOptional.isEmpty()){
            return "pageNotFound";
        }

        model.addAttribute("employee", credentialsOptional.get().getEmployee());
        return "employee/profile";
    }
}
