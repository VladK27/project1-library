package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.karelin.project.models.Employee;
import ru.karelin.project.models.EmployeeCredentials;
import ru.karelin.project.security.EmployeeDetails;
import ru.karelin.project.services.EmployeeCredentialsService;
import ru.karelin.project.services.EmployeeService;
import ru.karelin.project.utility.EmployeeCredentialsValidator;
import ru.karelin.project.utility.EmployeeValidator;

import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeCredentialsService employeeCredentialsService;
    private final EmployeeCredentialsValidator empCredentialsValidator;
    private final EmployeeService employeeService;
    private final EmployeeValidator employeeValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(EmployeeCredentialsService employeeCredentialsService, PasswordEncoder passwordEncoder, EmployeeCredentialsValidator empCredentialsValidator, EmployeeService employeeService, EmployeeValidator employeeValidator) {
        this.employeeCredentialsService = employeeCredentialsService;
        this.passwordEncoder = passwordEncoder;
        this.empCredentialsValidator = empCredentialsValidator;
        this.employeeService = employeeService;
        this.employeeValidator = employeeValidator;
    }

    @GetMapping("/profile")
    public String profilePage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof String
                && authentication.getPrincipal().equals("anonymousUser") ){
            return "errors/403";
        }

        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

        Optional<EmployeeCredentials> credentialsOptional =
                employeeCredentialsService.findByUsernameAndLoadEmployee(employeeDetails.getUsername());

        if(credentialsOptional.isEmpty()){
            return "errors/404";
        }

        model.addAttribute("employee", credentialsOptional.get().getEmployee());
        model.addAttribute("role", credentialsOptional.get().getRole().toString().substring(5));
        return "employee/profile";
    }

    @GetMapping("/edit")
    public String editPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof String
                && authentication.getPrincipal().equals("anonymousUser") ){
            return "errors/403";
        }

        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

        Employee employee =
                employeeCredentialsService.findByUsernameAndLoadEmployee
                                (employeeDetails.getUsername())
                        .get().getEmployee();

        model.addAttribute("employee", employee);

        return "employee/edit";
    }

    @PostMapping("/edit")
    public String performEdit(
            Model model,
            @ModelAttribute("password") String password,
            @ModelAttribute("new_password") String newPassword,
            @ModelAttribute("employee") @Valid Employee employee,
            BindingResult bindingResult
    )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

        if(!passwordEncoder.matches(password, employeeDetails.getPassword()))
        {
            model.addAttribute("oldPasswordError", "Wrong password!");
        }

        EmployeeCredentials newEmployeeCredentials = null;
        BindingResult errorsCredentials = null;
        if(!newPassword.isEmpty()){
            newEmployeeCredentials =
                    new EmployeeCredentials(employeeDetails.getUsername(), newPassword);
            errorsCredentials =
                    empCredentialsValidator.validateUpdatedAndGetBindingResult(newEmployeeCredentials);
            model.addAttribute("errorsCredentials", errorsCredentials);
        }

        employeeValidator.validate(employee, bindingResult);

        //if fields contains any errors
        if(model.containsAttribute("oldPasswordError") ||
                (model.containsAttribute("errorsCredentials") && errorsCredentials.hasErrors())
                || bindingResult.hasErrors())
        {
            return "employee/edit";
        }

        if(!newPassword.isEmpty()){
            employeeCredentialsService.update(newEmployeeCredentials);
        }
        employeeService.save(employee);

        return "redirect:/employee/profile";
    }
}
