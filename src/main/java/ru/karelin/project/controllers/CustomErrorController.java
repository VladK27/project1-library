package ru.karelin.project.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String errorPage(HttpServletRequest request, Model model){
        Integer errorCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        switch (errorCode){
            case 404 -> {
                return "errors/404";
            }
            case 500 -> {
                return "errors/500";
            }
            case 403 -> {
                return "errors/403";
            }
            default -> {
                model.addAttribute("errorCode", errorCode);

                return "errors/default-error";
            }
        }
    }
}
