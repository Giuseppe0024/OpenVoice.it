package it.aulab.final_project_giuseppe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.final_project_giuseppe.dtos.UserDto;
import it.aulab.final_project_giuseppe.services.UserService;
import it.aulab.final_project_giuseppe.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller 
public class UserController {

    @Autowired 
    private UserService userService;

    // Rotta di home
    @GetMapping
    public String home() {
        return "home";
    }

    // rotta per la registrazione
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    // rotta per il login
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // Rotta POST per il salvataggio della registrazione
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request,
                           HttpServletResponse response) {

    // Verifica se l'email inserita è già presente a database
    User existingUser = userService.findUserByEmail(userDto.getEmail());

    if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
        result.rejectValue("email", null, "È già presente un account registrato con questa email");
    }

    // Se ci sono errori di validazione o l'email è duplicata, ricarica la pagina con gli errori
    if (result.hasErrors()) {
        model.addAttribute("user", userDto);
        return "auth/register";
    }

    // Salvataggio tramite service
    userService.saveUser(userDto, redirectAttributes, request, response);

    redirectAttributes.addFlashAttribute("successMessage", "Registrazione avvenuta con successo!");

    return "redirect:/";
}
}