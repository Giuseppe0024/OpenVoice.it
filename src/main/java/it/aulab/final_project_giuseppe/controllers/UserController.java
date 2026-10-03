package it.aulab.final_project_giuseppe.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.final_project_giuseppe.services.ArticleService;
import it.aulab.final_project_giuseppe.services.CategoryService;
import it.aulab.final_project_giuseppe.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import it.aulab.final_project_giuseppe.models.User;
import it.aulab.final_project_giuseppe.repositories.CareerRequestRepository;
import it.aulab.final_project_giuseppe.dtos.ArticleDto;
import it.aulab.final_project_giuseppe.dtos.UserDto;

@Controller 
public class UserController {

    @Autowired 
    private UserService userService;

    @Autowired 
    private ArticleService articleService;

    @Autowired 
    private CareerRequestRepository careerRequestRepository;

    @Autowired 
    private CategoryService categoryService;

    // Rotta di home
    @GetMapping
    public String home(Model viewModel) {
        List<ArticleDto> articles = articleService.readAll();

        //ordino e invio al template gli articoli ordinati in modo decrescente
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());

        List<ArticleDto> lastThreeArticle = articles.stream().limit(3).collect(Collectors.toList());

        viewModel.addAttribute("articles", lastThreeArticle);

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

    //Rotta per la ricerca degli articoli in base all'utente
    @GetMapping("/search/{id}")
    public String userArticleSearch(@PathVariable("id") Long id, Model viewModel) {
        User user = userService.find(id);
        viewModel.addAttribute("title", "Tutti gli articoli trovati per l'utente: " + user.getUsername());
        
        List<ArticleDto> articles = articleService.searchByAuthor(user);
        viewModel.addAttribute("article", articles);

        return "article/articles";
    }

    //Rotta per la dashboard dell'admin
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model viewModel) {
        viewModel.addAttribute("title", "Richieste ricevute");
        viewModel.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "admin/dashboard";
    }
}